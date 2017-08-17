package org.myproject.sentiment.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.sentiment.interfaces.SentimentExtractor;
import org.myproject.sentiment.engine.SocialTopicSentimentEngine;
import org.myproject.sentiment.engine.UserSentiment;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;

public class TwitterQueryService implements SentimentExtractor {
	private static final String LANG_PARAM = "lang";
	public static final String DEFAULT_LANG = "en";
	private static final Logger logger = LogManager.getLogger(TwitterQueryService.class);
	
	private Twitter twitter = null;
	
	public TwitterQueryService() {
		PropertyConfiguration config = new PropertyConfiguration(
				TwitterQueryService.class.getResourceAsStream("/twitter4j.properties"));
		twitter = (new TwitterFactory(config)).getInstance();
		try {
			twitter.getRateLimitStatus()
				   .entrySet()
				   .stream()
				   .forEach(entry -> logger.info(entry.getKey() + " " + entry.getValue().getLimit()));
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public List<UserSentiment> getSentiments(String topic, Map<String, String> paramMapping) {		
		List<UserSentiment> sentiments = new ArrayList<>();
		try {
			Query query = buildQuery(topic, paramMapping);
			QueryResult queryResult = twitter.search(query);
			if(queryResult == null) {
				return sentiments;
			}
			
			logger.info("number of tweets returned for topic " + topic + " is "  
						+ queryResult.getTweets().size());
			
			return queryResult.getTweets()
					   .stream()
					   .map(this::createSentiment)
					   .collect(Collectors.toList());
		} catch (TwitterException e) {
			logger.error(e.toString());
		}
		return sentiments;
	}

	private UserSentiment createSentiment(Status status) {
		UserSentiment sentiment = new UserSentiment(String.valueOf(status.getId()), 
				status.getText(), status.getUser().getName(), 
				status.getUser().getLocation(), SocialTopicSentimentEngine.TWITTER);
		if(status.isRetweet()) {
			sentiment.setParentId(String.valueOf(status.getRetweetedStatus().getId()));
			
		}
		try {
			ResponseList<Status> retweets = twitter.getRetweets(status.getId());
			List<String> retweetIds = 
					retweets.stream()
					.map(s -> String.valueOf(s.getId()))
					.collect(Collectors.toList());
			sentiment.setLinkedSentiments(retweetIds);
		} catch (TwitterException e) {
			logger.info(e.toString());
		}
		return sentiment;
	}

	private Query buildQuery(String topic, Map<String, String> paramMapping) {
		Query query = new Query(topic);
		query.setLang(paramMapping.getOrDefault(LANG_PARAM, DEFAULT_LANG));
		return query;
	}
	
	public static void main(String[] args) throws TwitterException {
		TwitterQueryService twitterQueryService = new TwitterQueryService();
		twitterQueryService.getSentiments(args[0], new HashMap<>())
						   .forEach(logger::info);
		
//		PropertyConfiguration config = new PropertyConfiguration(
//				TwitterQueryService.class.getResourceAsStream("/twitter4j.properties"));
//		Twitter twitter = (new TwitterFactory(config)).getInstance();
//	    Query query = new Query("twitter");
//	    QueryResult result = twitter.search(query);
//	    for (Status status : result.getTweets()) {
//	        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
//	    }
//	    System.out.println(result.getCount());

	}

}
