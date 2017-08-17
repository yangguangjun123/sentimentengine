package org.myproject.sentiment.facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.sentiment.interfaces.SentimentExtractor;
import org.myproject.sentiment.engine.SocialTopicSentimentEngine;
import org.myproject.sentiment.engine.UserSentiment;

import java.lang.Thread;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FacebookQueryService implements SentimentExtractor {
	private static final int NUMBER_OF_POSTS_PER_PAGE = 5;
    private static final int NUMBER_OF_CONCURRENT_REQUESTS = 50;
    private static final int MAX_SIZE_OF_THREAD_POOL = 100;
    
    private final Executor executor = Executors.newFixedThreadPool(
    		Math.min(NUMBER_OF_CONCURRENT_REQUESTS, MAX_SIZE_OF_THREAD_POOL), FacebookQueryService::newThread);
    
    private CopyOnWriteArrayList<UserSentiment> userSentiments = new CopyOnWriteArrayList<>();
    private ConcurrentHashMap<String, String> locationLookup = new ConcurrentHashMap<>();
    private LongAdder count = new LongAdder();
    
    private static final Logger logger = LogManager.getLogger(FacebookQueryService.class);
    
    private DefaultFacebookClient facebookClient = null;    
    
	public FacebookQueryService() {
		AuthService auth = new AuthService();
		String accessToken = auth.getAccessToken();
		facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_8);
	}
	
	@Override
	public List<UserSentiment> getSentiments(String topic, Map<String, String> paramMapping) {
		List<Page> topicPages = getPages(topic);
		CompletableFuture<?>[] futures = topicPages.stream()
				.map(page -> CompletableFuture.supplyAsync(() -> getPosts(page.getId()), executor))
				.map(future -> future
						.thenCompose(posts -> CompletableFuture.supplyAsync(() -> getAllPostCommentsStream(posts, locationLookup))))				
				.map(future -> future.thenAccept(postfutures -> {
					CompletableFuture<?>[] fs = postfutures
							.map(commentsFuture -> commentsFuture.thenAccept(commentList -> {
								logger.info("total number of comments in the list: " + commentList.size());
								commentList.stream()
                                        .filter(Objects::nonNull)
                                        .forEach(this::consumeComment);

							})).toArray(CompletableFuture[]::new);
					CompletableFuture.allOf(fs).join();
				})).toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();
		logger.info("total number of sentiment comments: " + count.intValue());
		return userSentiments;
	}

	private void consumeComment(Comment comment) {
		logger.info("message: " + comment.getMessage());
		logger.info("message from: " + comment.getFrom().getName());
		logger.info("message from(id)" + comment.getFrom().getId());
		UserSentiment userSentiment = new UserSentiment(comment.getId(), comment.getMessage(), 
				comment.getFrom().getName(), locationLookup.get(comment.getFrom().getId()), 
				SocialTopicSentimentEngine.FACEBOOK);
		if(Objects.isNull(comment.getComments()) || Objects.isNull(comment.getComments().getData())) {
			logger.info("no reply comments" );
		} else {
			logger.info("size of reply comments: " + comment.getComments().getData().size());
			List<String> ids = 
					comment.getComments()
                           .getData()
                           .stream()
                           .map(Comment::getId)
                           .collect(toList());
			userSentiment.setLinkedSentiments(ids);

		}
		if(Objects.isNull(comment.getParent())) {
			userSentiment.setParentId(comment.getId());
		}
		userSentiments.add(userSentiment);
		count.increment();
	}
	
	private List<Page> getPages(String topic) {
		Connection<Page> publicSearch =
				  facebookClient.fetchConnection("search", Page.class,
				    Parameter.with("q", topic), Parameter.with("type", "page"));
		return publicSearch.getData();
	}
		
	private List<Post> getPosts(String pageId ) {
		Connection<Post> publicSearch = facebookClient.fetchConnection(pageId + "/feed", Post.class,
				  Parameter.with("limit", NUMBER_OF_POSTS_PER_PAGE));
		return publicSearch.getData();
	}
	
	private void populateUserLocationInfo(Comment comment, ConcurrentHashMap<String, String> locationLookup) {
		String userId = comment.getFrom().getId();
		if(Objects.isNull(locationLookup.get(userId)) || 
				StringUtils.equals("UNKNOWN", locationLookup.get(userId))) {
			User user = facebookClient.fetchObject(userId, User.class);
			if(Objects.isNull(user.getLocation()) || 
					StringUtils.isBlank(user.getLocation().getName())) {
				logger.info("user location: " + user.getLocation() + " for user id " + userId);
				locationLookup.put(userId, "UNKNOW");
			} else {
				locationLookup.put(userId, user.getLocation().getName());
			}
		}
	}
	
	private List<Comment> getAllPostComments(String postId, ConcurrentHashMap<String, String> locationLookup){
//      JsonObject jsonObject = facebookClient.fetchObject(postId + "/comments", JsonObject.class, 
//            Parameter.with("summary", true), Parameter.with("limit", 1));
//      long commentsTotalCount = jsonObject.get("summary").asObject().getLong("total_count", 0);

      logger.info("Getting all comments for post id " + postId);
      Connection<Comment> comments = facebookClient.fetchConnection(postId + "/comments", 
              Comment.class, Parameter.with("limit", 50000), Parameter.with("offset", 0));
      
      CompletableFuture<?>[] futures =
    		  comments.getData()
      			     .stream()
      			     .filter(Objects::nonNull)
      			     .filter(comment -> StringUtils.isNotBlank(comment.getMessage()))
      			     .filter(comment -> Objects.nonNull(comment.getFrom()))
      			     .filter(comment -> StringUtils.isNotBlank(comment.getFrom().getId()))
      			     .map(comment -> CompletableFuture.runAsync(() -> this.populateUserLocationInfo(comment, locationLookup), executor))
      			     .toArray(CompletableFuture[]::new);
      CompletableFuture.allOf(futures).join();
      
      return comments.getData()
    		  .stream()
    		  .filter(Objects::nonNull)
    		  .filter(comment -> StringUtils.isNotBlank(comment.getMessage()))
    		  .filter(comment -> Objects.nonNull(comment.getFrom()))
    		  .filter(comment -> StringUtils.isNotBlank(comment.getFrom().getId()))
    		  .collect(toList());
	}
	
	private Stream<CompletableFuture<List<Comment>>> getAllPostCommentsStream(List<Post> posts, ConcurrentHashMap<String, String>locationLookup) {
//      JsonObject jsonObject = facebookClient.fetchObject(postId + "/comments", JsonObject.class, 
//            Parameter.with("summary", true), Parameter.with("limit", 1));
//      long commentsTotalCount = jsonObject.get("summary").asObject().getLong("total_count", 0);
		return posts.stream()
					.map(post -> CompletableFuture.supplyAsync(() ->
								getAllPostComments(post.getId(), locationLookup), executor));
	}

    private static Thread newThread(Runnable r) {
        Objects.requireNonNull(r);
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }

	public static void main(String[] args) {
		AuthService auth = new AuthService();
		String accessToken = auth.getAccessToken();
		logger.trace("access token: {}", () -> accessToken );
		
		FacebookQueryService queryService = new FacebookQueryService();
		List<Post> posts = queryService.getPosts("661181370658667");
					posts.stream()
						 .forEach(logger::info);

		AtomicInteger currentCount = new AtomicInteger(0);
		queryService.getAllPostComments("661181370658667_979813902118868", new ConcurrentHashMap<>())
					.stream()
					.map(comment -> {
							String message = comment.getMessage().replaceAll("\n", " ")
													.replaceAll("\r", " ");
							return "comment [" + currentCount.incrementAndGet() + "]: " + 
										comment.getFrom().getName() + " ## " + message; 
					})
					.forEach(logger::info);

		logger.info("topic: " + args[0]);
		List<UserSentiment> sentiments = queryService.getSentiments(args[0], new HashMap<>());
		logger.info("total number of user sentiments created: " + sentiments.size());
		sentiments.forEach(logger::info);
	}
	
}
