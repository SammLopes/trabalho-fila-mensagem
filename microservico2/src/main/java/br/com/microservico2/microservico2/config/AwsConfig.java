package br.com.microservico2.microservico2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import java.net.URI;


@Configuration
public class AwsConfig {
    
    @Bean
    public SqsClient sqsClient() {
        String endpoint = System.getenv("AWS_ENDPOINT");
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .endpointOverride(URI.create("http://localstack:4566")) 
                .build();
    }
}
