package org.myproject.sentiment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.sentiment.engine.SocialTopicSentimentEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Main Application - run social topic sentiment engine
 *
 */
public class MainApplication
{
	private static final Logger logger = LogManager.getLogger(MainApplication.class);
	
    public static void main( String[] args )
    {
        SocialTopicSentimentEngine engine = new SocialTopicSentimentEngine();
        logger.info("start social topic engine");
        logger.info("topic: " + args[0]);
        Map<String, String> paramMap = new HashMap<>();
        if(args.length > 1) {
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
