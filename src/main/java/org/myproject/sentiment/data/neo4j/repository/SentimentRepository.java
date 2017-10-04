package org.myproject.sentiment.data.neo4j.repository;

import org.myproject.sentiment.data.neo4j.domain.Sentiment;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface SentimentRepository<T extends Sentiment> extends GraphRepository<T> {

	// derived finder
	// Sentiment findBySourceId(String sourceId);
	
//	@Query("MATCH (sentiment:Sentiment)<-[r:LINKED_TO]-(other:Sentiment) "
//			+ "WHERE id(sentiment) = {sentiment} RETURN other")
//	Set<Sentiment> getSentimentsLinkedFrom(@Param("sentiment") Sentiment sentiment);
//
//	@Query("MATCH (sentiment:Sentiment)-[r:LINKED_TO]->(other:Sentiment) "
//			+ "WHERE id(sentiment) = {sentiment} RETURN other")
//	Set<Sentiment> getSentimentsLinkedTo(@Param("sentiment") Sentiment sentiment);
	
}
