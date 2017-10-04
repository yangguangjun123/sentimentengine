package org.myproject.sentiment.data.neo4j.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "FacebookSentiment")
public class FacebookSentiment extends Sentiment {
    public FacebookSentiment(String id, String text, String authorName, String authorLocation) {
        super(id, text, authorName, authorLocation);
    }
}
