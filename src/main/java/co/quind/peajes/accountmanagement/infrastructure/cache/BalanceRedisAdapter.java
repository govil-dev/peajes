package co.quind.peajes.accountmanagement.infrastructure.cache;

import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import co.quind.peajes.accountmanagement.domain.port.out.BalanceCachePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceRedisAdapter implements BalanceCachePort {

    private static final String KEY_PREFIX = "balance:";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.cache.balance.ttl-seconds:10}")
    private long ttlSeconds;

    @Override
    public Mono<BalanceCacheEntry> get(String accountId) {
        return redisTemplate.opsForValue()
            .get(KEY_PREFIX + accountId)
            .flatMap(json -> {
                try {
                    return Mono.just(objectMapper.readValue(json, BalanceCacheEntry.class));
                } catch (JsonProcessingException e) {
                    log.warn("Failed to deserialize balance cache for accountId={}: {}", accountId, e.getMessage());
                    return Mono.empty();
                }
            });
    }

    @Override
    public Mono<Void> put(String accountId, BalanceCacheEntry entry) {
        try {
            String json = objectMapper.writeValueAsString(entry);
            return redisTemplate.opsForValue()
                .set(KEY_PREFIX + accountId, json, Duration.ofSeconds(ttlSeconds))
                .doOnSuccess(ok -> log.debug("Balance cached: accountId={}, ttl={}s", accountId, ttlSeconds))
                .then();
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize balance cache for accountId={}", accountId, e);
            return Mono.empty();
        }
    }
}
