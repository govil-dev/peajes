package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.event.*;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import java.time.Instant;
import java.util.ArrayList;
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
	private final Instant processedAt;
	private final TransactionStatus status;
	private final DeclineReason declineReason;
	private final List<Object> domainEvents = new ArrayList<>();

	private TollPass(UUID id, PassId passId, TagId tagId, StationId stationId, LaneId laneId,
					VehicleClass vehicleClass, MoneyAmount tariff, Instant detectedAt,
					Instant processedAt, TransactionStatus status, DeclineReason declineReason) {
		this.id = id;
		this.passId = passId;
		this.tagId = tagId;
		this.stationId = stationId;
		this.laneId = laneId;
		this.vehicleClass = vehicleClass;
		this.tariff = tariff;
		this.detectedAt = detectedAt;
		this.processedAt = processedAt;
		this.status = status;
		this.declineReason = declineReason;
	}

	public static TollPass authorize(PassId passId, TagId tagId, StationId stationId,
									 LaneId laneId, VehicleClass vehicleClass,
									 MoneyAmount tariff, Instant detectedAt,
									 String accountId, String balanceAfter) {
		TollPass tollPass = new TollPass(UUID.randomUUID(), passId, tagId, stationId, laneId,
			vehicleClass, tariff, detectedAt, Instant.now(), TransactionStatus.AUTHORIZED, null);
		tollPass.raiseTollPassRegistered();
		tollPass.raiseTransactionAuthorized(accountId, balanceAfter);
		return tollPass;
	}

	/** Alias of {@link #authorize} for test builders. */
	public static TollPass authorized(PassId passId, TagId tagId, StationId stationId,
									  LaneId laneId, VehicleClass vehicleClass,
									  MoneyAmount tariff, Instant detectedAt,
									  String accountId, String balanceAfter) {
		return authorize(passId, tagId, stationId, laneId, vehicleClass, tariff, detectedAt,
			accountId, balanceAfter);
	}

	public static TollPass decline(PassId passId, TagId tagId, StationId stationId,
								   LaneId laneId, DeclineReason reason, Instant detectedAt) {
		TollPass tollPass = new TollPass(UUID.randomUUID(), passId, tagId, stationId, laneId,
			null, null, detectedAt, Instant.now(), TransactionStatus.DECLINED, reason);
		tollPass.raiseTollPassRegistered();
		tollPass.raiseTransactionDeclined();
		if (reason == DeclineReason.INSUFFICIENT_BALANCE) {
			tollPass.raiseAccountBalanceLow();
		}
		return tollPass;
	}

	/** Full-context decline used when vehicleClass and tariff are known (e.g. balance check). */
	public static TollPass declined(PassId passId, TagId tagId, StationId stationId,
									LaneId laneId, VehicleClass vehicleClass, MoneyAmount tariff,
									DeclineReason reason, Instant detectedAt) {
		TollPass tollPass = new TollPass(UUID.randomUUID(), passId, tagId, stationId, laneId,
			vehicleClass, tariff, detectedAt, Instant.now(), TransactionStatus.DECLINED, reason);
		tollPass.raiseTollPassRegistered();
		tollPass.raiseTransactionDeclined();
		if (reason == DeclineReason.INSUFFICIENT_BALANCE) {
			tollPass.raiseAccountBalanceLow();
		}
		return tollPass;
	}

	public static TollPass declineWithNote(PassId passId, TagId tagId, StationId stationId,
										   LaneId laneId, DeclineReason reason, String operatorNote,
										   Instant detectedAt) {
		return decline(passId, tagId, stationId, laneId, reason, detectedAt);
	}

	/** Reconstructs a TollPass from persisted state — does NOT raise domain events. */
	public static TollPass fromPersisted(UUID id, PassId passId, TagId tagId, StationId stationId,
										 LaneId laneId, VehicleClass vehicleClass, MoneyAmount tariff,
										 TransactionStatus status, DeclineReason declineReason,
										 Instant detectedAt, Instant processedAt) {
		return new TollPass(id, passId, tagId, stationId, laneId, vehicleClass, tariff,
			detectedAt, processedAt, status, declineReason);
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

	private void raiseTransactionAuthorized(String accountId, String balanceAfter) {
		if (status == TransactionStatus.AUTHORIZED) {
			domainEvents.add(new TransactionAuthorized(
				UUID.randomUUID().toString(),
				passId.value(),
				accountId,
				tariff.toJsonString(),
				tariff.currency(),
				balanceAfter,
				processedAt.toString(),
				Instant.now().toString(),
				stationId.toString(),
				vehicleClass.name()
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

	public UUID id() { return id; }
	public PassId passId() { return passId; }
	public TagId tagId() { return tagId; }
	public StationId stationId() { return stationId; }
	public LaneId laneId() { return laneId; }
	public VehicleClass vehicleClass() { return vehicleClass; }
	public MoneyAmount tariff() { return tariff; }
	/** Alias for {@link #tariff()} — used by persistence and result mappers. */
	public MoneyAmount amount() { return tariff; }
	public Instant detectedAt() { return detectedAt; }
	public Instant processedAt() { return processedAt; }
	public TransactionStatus status() { return status; }
	public DeclineReason declineReason() { return declineReason; }

}
