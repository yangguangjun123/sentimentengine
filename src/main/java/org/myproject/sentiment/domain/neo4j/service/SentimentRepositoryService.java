package org.myproject.sentiment.domain.neo4j.service;

import org.myproject.sentiment.engine.SocialTopicSentimentEngine;
import org.myproject.sentiment.engine.UserSentiment;
import org.myproject.sentiment.domain.neo4j.entity.Sentiment;
import org.myproject.sentiment.domain.neo4j.repository.SentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentimentRepositoryService {
	private final SentimentRepository sentimentRepository;

	public SentimentRepositoryService() {
		sentimentRepository = null;
	}

	@Autowired
	public SentimentRepositoryService(SentimentRepository sentimentRepository) {
		this.sentimentRepository = sentimentRepository;
	}
	
	public void create(UserSentiment userSentiment) {
		Sentiment entity = new Sentiment(userSentiment.getId(), userSentiment.getText(), 
				userSentiment.getSource(), userSentiment.getUserName(), userSentiment.getUserLocation());
		sentimentRepository.save(entity);
	}
	
	public static void main(String[] args) {
		SentimentRepositoryService service = new SentimentRepositoryService();
		UserSentiment userSentiment = new UserSentiment("895197702857871360", "RT @vincecable: I agree with "
				+ "David Davis' ex chief of staff. The public should have a chance to exit"
				+ " from Brexit.  https://t.co/jHpbEFNNle…", "Stephen Bishop", "Bishop's Stortford, East",
				SocialTopicSentimentEngine.TWITTER);
		userSentiment.setParentId("895065078487232512");
		service.create(userSentiment);
	}

}
