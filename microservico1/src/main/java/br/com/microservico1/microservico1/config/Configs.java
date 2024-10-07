package br.com.microservico1.microservico1.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import java.net.URI;

@Configuration
public class Configs {
   
    @Bean
    public SqsClient sqsClient() {

        return SqsClient.builder()
        .credentialsProvider(ProfileCredentialsProvider.create("default"))
        .endpointOverride(URI.create("http://localhost:4566"))
        .region(Region.SA_EAST_1)
        .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
