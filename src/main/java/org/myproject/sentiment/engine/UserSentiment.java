package org.myproject.sentiment.engine;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserSentiment {
	private String id;
	private String text;
	private String userName;
	private String userLocation;
	private SentimentSource source;
	private String parentId;  // model retweets/parent comment
	private List<String> linkedSentiments;
	
	public UserSentiment(String id, String text, String userName, 
			String userLocation, SentimentSource source) {
		this.id = id;
		this.text = text;
		this.userName = userName;
		this.userLocation = userLocation;
		this.source = source;
		this.setLinkedSentiments(new ArrayList<>());
	}
	
	public String getText() {
		return text;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserLocation() {
		return userLocation;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	
	public SentimentSource getSource() {
		return source;
	}

	public void setSource(SentimentSource source) {
		this.source = source;
	}
		
	public String toString() {
		return "[ (id " + this.id + " tweet) "+ this.userName + " from " + this.userLocation 
				+ " post text on " + source + " ( linked sentiments [ " + 
				this.linkedSentiments.stream().collect(joining()) +
				" ], parent id [" + getParentId().orElse("") + "  ] ) ] - " + this.text; 
	}

	public List<String> getLinkedSentiments() {
		return linkedSentiments;
	}

	public void setLinkedSentiments(List<String> linkedSentiments) {
		this.linkedSentiments = linkedSentiments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Optional<String> getParentId() {
		return Optional.ofNullable(parentId);
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}