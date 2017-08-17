package org.myproject.sentiment.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.sentiment.facebook.FacebookQueryService;
import org.myproject.sentiment.interfaces.SentimentExtractor;
import org.myproject.sentiment.twitter.TwitterQueryService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SocialTopicSentimentEngine {
	public static final String TWITTER = "Twitter";
	public static final String FACEBOOK = "Facebook";
	
	private static final int MAX_EXECTOR_POOL_SIZE = 100;
	private final List<SentimentExtractor> extractors = Arrays.asList(new TwitterQueryService(),
			new FacebookQueryService());

    private final Executor executor = Executors.newFixedThreadPool(Math.min(extractors.size(),
            Math.min(extractors.size(), MAX_EXECTOR_POOL_SIZE)), SocialTopicSentimentEngine::newThread);
	
	private static final Logger logger = LogManager.getLogger(SocialTopicSentimentEngine.class);

	public void run(String topic, Map<String, String> params) {
		CompletableFuture<?>[] futures = extractors.stream()
			    	  .map(extractor -> CompletableFuture.supplyAsync(
			    		  () -> extractor.getSentiments(topic, params), executor))
			    	  .map(future -> future.thenAccept(this::printUserSentinment))
			    	  .toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();
	}

	private void printUserSentinment(List<UserSentiment> sentiments) {
		sentiments.forEach(logger::info);
	}

    private static Thread newThread(Runnable r) {
        Objects.requireNonNull(r);
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }

	public static void main(String[] args) {
		SocialTopicSentimentEngine engine = new SocialTopicSentimentEngine();
		logger.info("start social topic engine");
		logger.info("topic: " + args[0]);
		Map<String, String> paramMap = new HashMap<>();
		if(args.length > 1) {
			String queryParams = args[1];
			Arrays.stream(queryParams.split(";"))
				  .forEach(s -> {
					  String[] params = s.split(",");
					  paramMap.put(params[0], params[1]);
				  });
			logger.info("query parameters(map): " + paramMap);
		}

		engine.run(args[0], paramMap);
	}
}