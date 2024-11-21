package com.example.jejugudgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class JejugudgoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JejugudgoApplication.class, args);
    }

}
