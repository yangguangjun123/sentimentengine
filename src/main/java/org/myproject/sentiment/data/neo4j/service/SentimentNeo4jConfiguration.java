package org.myproject.sentiment.data.neo4j.service;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.rmi.server.RemoteServer;

@Configuration
@EnableNeo4jRepositories(basePackages = "org.myproject.sentimentengine.domain.neo4j.repository")
@EnableTransactionManagement
@ComponentScan("org.myproject.sentiment.domain.neo4j")
public class SentimentNeo4jConfiguration { // extends Neo4jConfiguration {

//    @Bean
//    public Neo4jServer neo4jServer() {
//        return new RemoteServer("http://localhost:7474");
//    }

    @Bean
    public SessionFactory sessionFactory() {
        // with domain entity base package(s)
        return new SessionFactory("org.myproject.sentimentengine.domain.neo4j.entity");
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

}
