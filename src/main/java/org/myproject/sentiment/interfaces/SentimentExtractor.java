package org.myproject.sentiment.interfaces;

import org.myproject.sentiment.engine.UserSentiment;

import java.util.List;
import java.util.Map;

/**
 * 
 * Extract comments from Facebook and tweets from Twitter
 * 
 * @author guangjun
 *
 */
public interface SentimentExtractor {
	
	List<UserSentiment> getSentiments(String topic, Map<String, String> paramMapping);
		
}
