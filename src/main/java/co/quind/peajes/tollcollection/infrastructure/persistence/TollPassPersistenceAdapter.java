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
        var e = new TollPassEntity();
        e.setPassId(p.passId().value());
        e.setTagId(p.tagId().value());
        e.setStationId(p.stationId().value());
        e.setLaneId(p.laneId().value());
        e.setVehicleClass(p.vehicleClass().name());
        e.setAmount(p.amount().amount());
        e.setCurrency(p.amount().currency());
        e.setStatus(p.status().name());
        e.setDeclineReason(p.declineReason() != null ? p.declineReason().name() : null);
        e.setDetectedAt(p.detectedAt());
        e.setProcessedAt(p.processedAt());
        return e;
    }

    private TollPass toDomain(TollPassEntity e) {
        return new TollPass(
                new PassId(e.getPassId()),
                new TagId(e.getTagId()),
                new StationId(e.getStationId()),
                new LaneId(e.getLaneId()),
                VehicleClass.valueOf(e.getVehicleClass()),
                new MoneyAmount(e.getAmount(), e.getCurrency()),
                TransactionStatus.valueOf(e.getStatus()),
                e.getDeclineReason() != null ? DeclineReason.valueOf(e.getDeclineReason()) : null,
                e.getDetectedAt(),
                e.getProcessedAt()
        );
    }
}
