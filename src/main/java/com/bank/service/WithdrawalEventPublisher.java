package com.bank.service;

import com.bank.model.dto.event.WithdrawalResult;
import com.bank.model.dto.event.WithdrawalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class WithdrawalEventPublisher {
    private final SnsClient snsClient;

    @Value("${bank.sns.withdrawal-topic-arn}")
    private String withdrawalTopicArn;

    @Async
    public void publishWithdrawalEventAsync(WithdrawalResult result) {
        try {
            WithdrawalEvent event = new WithdrawalEvent(
                    result.amount(),
                    result.accountId(),
                    result.transactionId(),
                    "SUCCESSFUL"
            );

            PublishRequest request = PublishRequest.builder()
                    .topicArn(withdrawalTopicArn)
                    .message(event.toJson())
                    .build();

            snsClient.publish(request);
            log.info("Successfully published withdrawal event for transaction: {}", result.transactionId());

        } catch (Exception e) {
            log.error("Failed to publish withdrawal event for transaction: {}", result.transactionId(), e);
            // Event publishing failure doesn't affect the main transaction
        }
    }
}
