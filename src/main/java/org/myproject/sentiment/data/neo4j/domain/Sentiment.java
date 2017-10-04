package org.myproject.sentiment.data.neo4j.domain;

import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity(label = "Sentiment")
public class Sentiment {
	@GraphId
	private  String graphId;

	private String id;
	private String text;
	private String authorName;
	private String authorLocation;
	
	@Relationship(type="LINKED_BY", direction=Relationship.INCOMING)
	private Set<Sentiment> linkedBy;

	@Relationship(type="LINKED_TO")
	private Set<Sentiment> linkedTo;

	public Sentiment(String id, String text, String authorName, String authorLocation) {
		super();
		this.id = id;
		this.text = text;
		this.authorName = authorName;
		this.authorLocation = authorLocation;
	}

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorLocation() {
		return authorLocation;
	}

	public void setAuthorLocation(String authorLocation) {
		this.authorLocation = authorLocation;
	}

    public Set<Sentiment> getLinkedBy() {
        return linkedBy;
    }

    public void setLinkedBy(Set<Sentiment> linkedBy) {
        this.linkedBy = linkedBy;
    }

    public Set<Sentiment> getLinkedTo() {
        return linkedTo;
    }

    public void setLinkedTo(Set<Sentiment> linkedTo) {
        this.linkedTo = linkedTo;
    }

    @Override
    public String toString() {
        return "Sentiment{" +
                "graphId='" + graphId + '\'' +
                ", id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorLocation='" + authorLocation + '\'' +
                ", linkedBy=" + linkedBy +
                ", linkedTo=" + linkedTo +
                '}';
    }
}
