package com.example.urlshortener.config;

import com.example.urlshortener.event.UrlClickEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic urlClicksTopic() {
        return new NewTopic("url-clicks", 3, (short) 1);
    }
}
