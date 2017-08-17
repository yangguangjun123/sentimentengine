package org.myproject.sentiment.domain.neo4j.entity;

import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Sentiment {
	@GraphId
	String id;
	
	String sourceId;
	String text;
	String sourceName;
	String authorName;
	String authorLocation;
	
	@Relationship(type="LINKED_TO", direction=Relationship.INCOMING)
	Set<Sentiment> linkedFrom;

	@Relationship(type="LINKED_TO", direction=Relationship.OUTGOING)
	Set<Sentiment> linkedTo;

	public Sentiment(String sourceId, String text, String sourceName, String authorName,
					 String authorLocation) {
		super();
		this.sourceId = sourceId;
		this.text = text;
		this.sourceName = sourceName;
		this.authorName = authorName;
		this.authorLocation = authorLocation;
	}

	public Set<Sentiment> getLinkedFrom() {
	    return linkedFrom;
    }

    public Set<Sentiment> getLinkedTo() {
	    return linkedTo;
    }

}
