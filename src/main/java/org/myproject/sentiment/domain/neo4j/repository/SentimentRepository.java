package org.myproject.sentiment.domain.neo4j.repository;

import java.util.Set;

import org.myproject.sentiment.domain.neo4j.entity.Sentiment;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface SentimentRepository extends GraphRepository<Sentiment> {

	// derived finder
	// Sentiment findBySourceId(String sourceId);
	
	@Query("MATCH (sentiment:Sentiment)<-[r:LINKED_TO]-(other:Sentiment) "
			+ "WHERE id(sentiment) = {sentiment} RETURN other")
	Set<Sentiment> getSentimentsLinkedFrom(@Param("sentiment") Sentiment sentiment);
	
	@Query("MATCH (sentiment:Sentiment)-[r:LINKED_TO]->(other:Sentiment) "
			+ "WHERE id(sentiment) = {sentiment} RETURN other")
	Set<Sentiment> getSentimentsLinkedTo(@Param("sentiment") Sentiment sentiment);
	
}
