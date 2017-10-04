package org.myproject.sentiment.data.neo4j.service;

import org.myproject.sentiment.data.neo4j.repository.FacebookSentimentRepository;
import org.myproject.sentiment.data.neo4j.repository.TwitterSentimentRepository;
import org.myproject.sentiment.data.neo4j.domain.FacebookSentiment;
import org.myproject.sentiment.data.neo4j.domain.TwitterSentiment;
import org.myproject.sentiment.engine.SentimentSource;
import org.myproject.sentiment.engine.UserSentiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentimentRepositoryService {

	@Autowired
	private FacebookSentimentRepository facebookSentimentRepository;

	@Autowired
	private TwitterSentimentRepository twitterSentimentRepository;

    private static final Logger logger = LoggerFactory.getLogger(SentimentRepositoryService.class);
	
	public void create(UserSentiment userSentiment) {
	    if(SentimentSource.FACEBOOK == userSentiment.getSource()) {
            FacebookSentiment entity = new FacebookSentiment(userSentiment.getId(), userSentiment.getText(),
                    userSentiment.getUserName(), userSentiment.getUserLocation());
            facebookSentimentRepository.save(entity);
        } else if(SentimentSource.TWITTER == userSentiment.getSource()){
            TwitterSentiment entity = new TwitterSentiment(userSentiment.getId(), userSentiment.getText(),
                    userSentiment.getUserName(), userSentiment.getUserLocation());
            twitterSentimentRepository.save(entity);
        } else {
            logger.error("invalid sentiment source - " + userSentiment.getSource());
        }
	}
	
	public static void main(String[] args) {
		SentimentRepositoryService service = new SentimentRepositoryService();
		UserSentiment userSentiment = new UserSentiment("895197702857871360", "RT @vincecable: I agree with "
				+ "David Davis' ex chief of staff. The public should have a chance to exit"
				+ " from Brexit.  https://t.co/jHpbEFNNle…", "Stephen Bishop", "Bishop's Stortford, East",
                SentimentSource.TWITTER);
		userSentiment.setParentId("895065078487232512");
		service.create(userSentiment);
	}

}
