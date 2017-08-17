package org.myproject.sentiment.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class OauthConfiguratorTest {
	
	@Test
	public void shouldReadOauthConsumerKey() {
		// given
		OauthConfigurator configurator = new OauthConfigurator();
		String expected = "Vekzsc2EUQIZHYOdZu7mH646N";
		
		// when
		String actual = configurator.getConsumerKey();
		
		//verify
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void shouldReadOauthConsumerSecret() {
		// given
		OauthConfigurator configurator = new OauthConfigurator();
		String expected = "enzDhnN6HZOmawus4NqOiGSpRIMLfMFUHo4R3csPyodLJi3aE7";

		// when
		String actual = configurator.getConsumerSecret();

		//verify
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void shouldReadOauthAccessToken() {
		// given
		OauthConfigurator configurator = new OauthConfigurator();
		String expected = "2981709041-ZxGcHyVPujBh516v7UHRNlNQEiFzlm9QvVYd4gZ";

		// when
		String actual = configurator.getAccessToken();

		//verify
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void shouldReadOauthAccessTokenSecret() {
		// given
		OauthConfigurator configurator = new OauthConfigurator();
		String expected = "FQuENIcALKbLZnNdk3wVLGlfVeoO9IycqSkE50E3cxCAS";

		// when
		String actual = configurator.getAccessTokenSecret();

		//verify
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void shouldSetDebugFlagAsTrue() {
		// given
		OauthConfigurator configurator = new OauthConfigurator();
		boolean expected = true;
		
		// when
		boolean actual = configurator.isDebugSet();
		
		// verify
		assertEquals(expected, actual);
	}
}