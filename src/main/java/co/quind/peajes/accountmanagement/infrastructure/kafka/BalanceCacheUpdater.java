package co.quind.peajes.accountmanagement.infrastructure.kafka;

import co.quind.peajes.accountmanagement.domain.event.AccountRecharged;
import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import co.quind.peajes.accountmanagement.domain.port.out.BalanceCachePort;
import co.quind.peajes.tollcollection.domain.event.TransactionAuthorized;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceCacheUpdater {

    private final BalanceCachePort balanceCachePort;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${app.account.kafka.topics.account-events:account.events.v1}",
        groupId = "${app.kafka.consumer.balance-cache.group-id:peajes-co-balance-cache}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void onAccountRecharged(ConsumerRecord<String, Object> record) {
        try {
            AccountRecharged event = objectMapper.convertValue(record.value(), AccountRecharged.class);
            if (event.accountId() == null || event.balanceAfter() == null) return;
            BalanceCacheEntry entry = new BalanceCacheEntry(
                new BigDecimal(event.balanceAfter()),
                event.currency() != null ? event.currency() : "COP",
                Instant.parse(event.occurredAt())
            );
            balanceCachePort.put(event.accountId(), entry)
                .doOnSuccess(v -> log.debug("Balance cache updated on AccountRecharged: accountId={}", event.accountId()))
                .subscribe();
        } catch (Exception e) {
            log.error("Error updating balance cache on AccountRecharged: offset={}", record.offset(), e);
        }
    }

    @KafkaListener(
        topics = "${app.toll.kafka.topics.toll-transactions:toll.transactions.v1}",
        groupId = "${app.kafka.consumer.balance-cache.group-id:peajes-co-balance-cache}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTransactionAuthorized(ConsumerRecord<String, Object> record) {
        try {
            TransactionAuthorized event = objectMapper.convertValue(record.value(), TransactionAuthorized.class);
            if (event.accountId() == null || event.balanceAfter() == null) return;
            BalanceCacheEntry entry = new BalanceCacheEntry(
                new BigDecimal(event.balanceAfter()),
                event.currency() != null ? event.currency() : "COP",
                Instant.parse(event.occurredAt())
            );
            balanceCachePort.put(event.accountId(), entry)
                .doOnSuccess(v -> log.debug("Balance cache updated on TransactionAuthorized: accountId={}", event.accountId()))
                .subscribe();
        } catch (Exception e) {
            log.error("Error updating balance cache on TransactionAuthorized: offset={}", record.offset(), e);
        }
    }
}
