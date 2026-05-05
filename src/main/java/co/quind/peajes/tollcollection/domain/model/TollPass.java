package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.event.*;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class TollPass {
	private final UUID id;
	private final PassId passId;
	private final TagId tagId;
	private final StationId stationId;
	private final LaneId laneId;
	private final VehicleClass vehicleClass;
	private final MoneyAmount tariff;
	private final Instant detectedAt;
	private TollPassStatus status;
	private DeclineReason declineReason;
	private final List<Object> domainEvents = new ArrayList<>();

	private TollPass(UUID id, PassId passId, TagId tagId, StationId stationId, LaneId laneId,
					VehicleClass vehicleClass, MoneyAmount tariff, Instant detectedAt,
					TollPassStatus status, DeclineReason declineReason) {
		this.id = id;
		this.passId = passId;
		this.tagId = tagId;
		this.stationId = stationId;
		this.laneId = laneId;
		this.vehicleClass = vehicleClass;
		this.tariff = tariff;
		this.detectedAt = detectedAt;
		this.status = status;
		this.declineReason = declineReason;
	}

	public static TollPass authorize(PassId passId, TagId tagId, StationId stationId,
									 LaneId laneId, VehicleClass vehicleClass,
									 MoneyAmount tariff, Instant detectedAt) {
		TollPass tollPass = new TollPass(UUID.randomUUID(), passId, tagId, stationId, laneId,
			vehicleClass, tariff, detectedAt, TollPassStatus.AUTHORIZED, null);
		tollPass.raiseTollPassRegistered();
		tollPass.raiseTransactionAuthorized();
		return tollPass;
	}

	public static TollPass decline(PassId passId, TagId tagId, StationId stationId,
								  LaneId laneId, DeclineReason reason, Instant detectedAt) {
		TollPass tollPass = new TollPass(UUID.randomUUID(), passId, tagId, stationId, laneId,
			null, null, detectedAt, TollPassStatus.DECLINED, reason);
		tollPass.raiseTollPassRegistered();
		tollPass.raiseTransactionDeclined();
		if (reason == DeclineReason.INSUFFICIENT_BALANCE) {
			tollPass.raiseAccountBalanceLow();
		}
		return tollPass;
	}

	private void raiseTollPassRegistered() {
		domainEvents.add(new TollPassRegistered(
			UUID.randomUUID().toString(),
			passId.value(),
			tagId.value(),
			stationId.toString(),
			laneId.toString(),
			vehicleClass != null ? vehicleClass.name() : null,
			tariff != null ? tariff.toJsonString() : null,
			tariff != null ? tariff.currency() : "COP",
			detectedAt.toString(),
			Instant.now().toString()
		));
	}

	private void raiseTransactionAuthorized() {
		if (status == TollPassStatus.AUTHORIZED) {
			domainEvents.add(new TransactionAuthorized(
				UUID.randomUUID().toString(),
				passId.value(),
				null,
				tariff.toJsonString(),
				tariff.currency(),
				null,
				Instant.now().toString(),
				Instant.now().toString()
			));
		}
	}

	private void raiseTransactionDeclined() {
		domainEvents.add(new TransactionDeclined(
			UUID.randomUUID().toString(),
			passId.value(),
			tagId.value(),
			declineReason.name(),
			Instant.now().toString(),
			Instant.now().toString()
		));
	}

	private void raiseAccountBalanceLow() {
		domainEvents.add(new AccountBalanceLow(
			UUID.randomUUID().toString(),
			null,
			null,
			"COP",
			"15000.00",
			Instant.now().toString()
		));
	}

	public List<Object> pullDomainEvents() {
		List<Object> events = new ArrayList<>(domainEvents);
		domainEvents.clear();
		return events;
	}

	// Getters
	public UUID id() { return id; }
	public PassId passId() { return passId; }
	public TagId tagId() { return tagId; }
	public StationId stationId() { return stationId; }
	public LaneId laneId() { return laneId; }
	public VehicleClass vehicleClass() { return vehicleClass; }
	public MoneyAmount tariff() { return tariff; }
	public Instant detectedAt() { return detectedAt; }
	public TollPassStatus status() { return status; }
	public DeclineReason declineReason() { return declineReason; }

}
