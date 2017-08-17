package org.myproject.sentiment.twitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class OauthService {
	private static final Logger logger = LogManager.getLogger(OauthService.class);

	public Twitter auth(String status) {
		OauthConfigurator configurator = new OauthConfigurator();
		TwitterFactory factory = new TwitterFactory();
	    AccessToken accessToken = new AccessToken(configurator.getAccessToken(), 
	    		configurator.getAccessTokenSecret());
	    Twitter twitter = factory.getInstance();
	    twitter.setOAuthConsumer(configurator.getConsumerKey(), configurator.getConsumerSecret());
	    twitter.setOAuthAccessToken(accessToken);
	    try {
			twitter.updateStatus(status);
		} catch (TwitterException e) {
			logger.error(e.toString());
		}
	    return twitter;
	}
}
