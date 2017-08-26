package org.myproject.sentiment.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class OauthConfigurator {
	private static final String CONFIG_FILE = "/twitter4j.properties";
	private static final String DEBUG_FLAG = "debug";
	private static final String CONSUMER_KEY = "oauth.consumerKey";
	private static final String CONSUMER_SECRET = "oauth.consumerSecret";
	private static final String ACCESS_TOKEN = "oauth.accessToken";
	private static final String ACCESS_TOKEN_SECRET = "oauth.accessTokenSecret";
    private static final Logger logger = LoggerFactory.getLogger(OauthConfigurator.class);

    private Properties properties = new Properties();

    public OauthConfigurator() {
		try {
			properties.load(OauthConfigurator.class.getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			logger.error("cannot read oauth properties from configuration file " + CONFIG_FILE);
			System.exit(1);
		}
	}
	
	public String getConsumerKey() {
		return properties.containsKey(CONSUMER_KEY) ? 
				(String) properties.get(CONSUMER_KEY) : "";
	}
	
	public String getConsumerSecret() {
		return properties.containsKey(CONSUMER_SECRET) ?
				properties.getProperty(CONSUMER_SECRET) : "";
	}
	
	public String getAccessToken() {
		return properties.containsKey(ACCESS_TOKEN) ?
				properties.getProperty(ACCESS_TOKEN) : "";
	}
	
	public String getAccessTokenSecret() {
		return properties.containsKey(ACCESS_TOKEN_SECRET) ?
				properties.getProperty(ACCESS_TOKEN_SECRET) : "";
	}
	
	public boolean isDebugSet() {
		return properties.containsKey(DEBUG_FLAG) && Boolean.parseBoolean(properties.getProperty(DEBUG_FLAG));
	}

	public static void main( String[] args )
	{
		logger.trace("Hello OauthConfigurator!");

		OauthConfigurator config = new OauthConfigurator();
		logger.trace("consumer key: " +  config.getConsumerKey());
		logger.trace("consumer secret: " + config.getConsumerSecret());
		logger.trace("access token: " + config.getAccessToken());
		logger.trace("access token secret: " + config.getAccessTokenSecret());
	}
}
