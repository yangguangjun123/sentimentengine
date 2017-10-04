package org.myproject.sentiment.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.myproject.sentiment.facebook.FacebookQueryService;
import org.myproject.sentiment.interfaces.SentimentExtractor;
import org.myproject.sentiment.twitter.TwitterQueryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class SocialTopicSentimentEngine implements CommandLineRunner {
	//public static final String TWITTER = "Twitter";
	//public static final String FACEBOOK = "Facebook";
	
	private static final int MAX_EXECTOR_POOL_SIZE = 100;
	private final List<SentimentExtractor> extractors = Arrays.asList(new TwitterQueryService(),
			new FacebookQueryService());

    private final Executor executor = Executors.newFixedThreadPool(Math.min(extractors.size(),
            Math.min(extractors.size(), MAX_EXECTOR_POOL_SIZE)), SocialTopicSentimentEngine::newThread);
	
	private static final Logger logger = LoggerFactory.getLogger(SocialTopicSentimentEngine.class);

	public void run(String topic, Map<String, String> params) {
		CompletableFuture<?>[] futures = extractors.stream()
			    	  .map(extractor -> CompletableFuture.supplyAsync(
			    		  () -> extractor.getSentiments(topic, params), executor))
			    	  .map(future -> future.thenAccept(this::printUserSentinment))
			    	  .toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();
	}

	public void consumeSentiments(List<UserSentiment> sentiments) {
		logger.info("consume sentinemnts - load sentiments into Ne04j database");

	}

	private void printUserSentinment(List<UserSentiment> sentiments) {
		sentiments.forEach(s -> logger.info(s.toString()));
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

	@Override
	public void run(String... args) throws Exception {
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

		run(args[0], paramMap);
	}
}