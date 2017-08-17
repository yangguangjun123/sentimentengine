package org.myproject.sentiment.facebook;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthConfigurator {
	private static final String CONFIG_FILE = "/facebook.properties";
	private static final String APP_ID = "app_id";
	private static final String APP_SECRET = "app_secret";
	
	private static final Logger logger = LoggerFactory.getLogger(AuthConfigurator.class);
	
	private Properties properties = new Properties();
	
	public AuthConfigurator() {
		try {
			properties.load(AuthConfigurator.class.getResourceAsStream(CONFIG_FILE));
		} catch (IOException e) {
			logger.error("cannot read oauth properties from configuration file " + CONFIG_FILE);
			System.exit(1);
		}
	}
	
	public String getAppId() {
		return properties.containsKey(APP_ID) ? properties.getProperty(APP_ID) : "";
	}
	
	public String getAppSecret() {
		return properties.containsKey(APP_SECRET) ? properties.getProperty(APP_SECRET) : "";
	}
}
