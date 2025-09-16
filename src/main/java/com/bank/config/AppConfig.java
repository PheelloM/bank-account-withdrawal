package com.bank.config;

import com.bank.util.TransactionIdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.UUID;

@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {
    @Bean
    public SnsClient snsClient(Region region) {
        return SnsClient.builder()
                .region(region)
                .build();
    }

    @Bean
    public Region awsRegion(@Value("${spring.cloud.aws.sns.region}") String region) {
        return Region.of(region);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

    @Bean
    public TransactionIdGenerator transactionIdGenerator() {
        return () -> UUID.randomUUID().toString();
    }
}
