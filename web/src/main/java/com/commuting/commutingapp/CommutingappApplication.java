package com.commuting.commutingapp;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.commuting.commutingapp.config.MongoDbAppender;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication ( exclude = {SecurityAutoConfiguration.class} )
@EnableScheduling
public class CommutingappApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CommutingappApplication.class, args);
		context.start();
		addCustomAppender(context, (LoggerContext) LoggerFactory.getILoggerFactory());
	}

	private static void addCustomAppender(ConfigurableApplicationContext context, LoggerContext loggerContext) {
		Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.addAppender(context.getBean(MongoDbAppender.class));
	}
}
