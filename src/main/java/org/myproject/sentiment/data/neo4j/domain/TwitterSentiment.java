package org.myproject.sentiment.data.neo4j.domain;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "TwitterSentiment")
public class TwitterSentiment extends Sentiment {

    public TwitterSentiment(String id, String text, String authorName, String authorLocation) {
        super(id, text, authorName, authorLocation);
    }
}
