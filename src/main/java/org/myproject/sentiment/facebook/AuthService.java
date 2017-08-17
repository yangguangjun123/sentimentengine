package org.myproject.sentiment.facebook;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Version;


public class AuthService {	
	public String getAccessToken() {
		AuthConfigurator auth = new AuthConfigurator();
		AccessToken accessToken =
				  new DefaultFacebookClient(Version.LATEST).obtainAppAccessToken(auth.getAppId(),
				    auth.getAppSecret());
		return accessToken.getAccessToken();
	}
}
