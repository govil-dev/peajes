package co.quind.peajes.tollcollection.infrastructure.kafka;

import co.quind.peajes.tollcollection.domain.model.TcTransactionEntry;
import co.quind.peajes.tollcollection.domain.event.TransactionAuthorized;
import co.quind.peajes.tollcollection.domain.port.out.TransactionHistoryRepository;
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
public class TransactionHistoryConsumer {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${app.toll.kafka.topics.toll-transactions:toll.transactions.v1}",
        groupId = "${app.kafka.consumer.transaction-history.group-id:peajes-co-transaction-history}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTransactionAuthorized(ConsumerRecord<String, Object> record) {
        try {
            TransactionAuthorized event = objectMapper.convertValue(record.value(), TransactionAuthorized.class);
            if (event.accountId() == null || event.passId() == null) return;

            transactionHistoryRepository.existsByTransactionId(event.passId())
                .flatMap(exists -> {
                    if (exists) {
                        log.debug("Transaction already in history (idempotent): passId={}", event.passId());
                        return reactor.core.publisher.Mono.empty();
                    }
                    TcTransactionEntry entry = new TcTransactionEntry(
                        event.passId(),
                        event.accountId(),
                        event.stationId() != null ? event.stationId() : "UNKNOWN",
                        new BigDecimal(event.tariffAmount()),
                        event.currency() != null ? event.currency() : "COP",
                        event.vehicleClass() != null ? event.vehicleClass() : "UNKNOWN",
                        Instant.parse(event.authorizedAt())
                    );
                    return transactionHistoryRepository.save(entry)
                        .doOnSuccess(v -> log.debug("Transaction saved to history: passId={}, accountId={}",
                            event.passId(), event.accountId()));
                })
                .subscribe();
        } catch (Exception e) {
            log.error("Error processing TransactionAuthorized for history: offset={}", record.offset(), e);
        }
    }
}
