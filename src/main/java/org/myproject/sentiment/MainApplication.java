package org.myproject.sentiment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.myproject.sentiment.engine.SocialTopicSentimentEngine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Application - run social topic sentiment engine
 *
 */
@EnableAutoConfiguration
@EntityScan("org.myproject.sentiment.data.neo4j.domain")
public class MainApplication implements CommandLineRunner
{
	private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
	
    public static void main( String[] args )
    {
        // run Spring boot application
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SocialTopicSentimentEngine engine = new SocialTopicSentimentEngine();
        logger.info("start social topic engine");
        logger.info("topic: " + args[0]);
        Map<String, String> paramMap = new HashMap<>();
        if (args.length > 1) {
            String queryParams = args[1];
            Arrays.stream(queryParams.split(";"))
                    .forEach(s -> {
                        String[] params = s.split(",");
                        paramMap.put(params[0], params[1]);
                    });
            logger.info("query parameters(map): " + paramMap);
        }

        engine.run(args[0], paramMap);
    }
}
