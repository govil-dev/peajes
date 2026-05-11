package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.DeclineReason;
import co.quind.peajes.tollcollection.domain.model.TollPass;
import co.quind.peajes.tollcollection.domain.model.TransactionStatus;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.out.TollPassRepository;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TollPassPersistenceAdapter implements TollPassRepository {

    private final TollPassR2dbcRepository r2dbcRepository;

    public TollPassPersistenceAdapter(TollPassR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<TollPass> findByPassId(PassId passId) {
        return r2dbcRepository.findByPassId(passId.value()).map(this::toDomain);
    }

    @Override
    public Mono<TollPass> save(TollPass tollPass) {
        return r2dbcRepository.save(toEntity(tollPass)).map(this::toDomain);
    }

    private TollPassEntity toEntity(TollPass p) {
        return new TollPassEntity(
                p.id(),
                p.passId().value(),
                p.tagId().value(),
                p.stationId().value(),
                p.laneId().value(),
                p.vehicleClass() != null ? p.vehicleClass().name() : null,
                p.amount() != null ? p.amount().amount() : null,
                p.amount() != null ? p.amount().currency() : "COP",
                p.status().name(),
                p.declineReason() != null ? p.declineReason().name() : null,
                p.detectedAt(),
                p.processedAt()
        );
    }

    private TollPass toDomain(TollPassEntity e) {
        return TollPass.fromPersisted(
                e.id(),
                new PassId(e.passId()),
                new TagId(e.tagId()),
                new StationId(e.stationId()),
                new LaneId(e.laneId()),
                e.vehicleClass() != null ? VehicleClass.valueOf(e.vehicleClass()) : null,
                e.tariffAmount() != null ? new MoneyAmount(e.tariffAmount(), e.currency()) : null,
                TransactionStatus.valueOf(e.status()),
                e.declineReason() != null ? DeclineReason.valueOf(e.declineReason()) : null,
                e.detectedAt(),
                e.processedAt()
        );
    }
}
