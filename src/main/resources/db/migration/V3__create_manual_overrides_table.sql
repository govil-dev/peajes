-- Manual Overrides table for HU-002: Gestionar paso vehicular con excepción

CREATE TABLE IF NOT EXISTS manual_overrides (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    override_id             VARCHAR(36) NOT NULL UNIQUE,
    station_id              VARCHAR(50) NOT NULL,
    lane_id                 VARCHAR(50) NOT NULL,
    license_plate           VARCHAR(20) NOT NULL,
    vehicle_class           VARCHAR(20) NOT NULL,
    reason                  VARCHAR(50) NOT NULL,
    operator_id             VARCHAR(50) NOT NULL,
    amount                  NUMERIC(15, 2) NOT NULL,
    lpr_photo_url           VARCHAR(500) NOT NULL,
    registered_at           TIMESTAMPTZ NOT NULL,
    requires_admin_approval BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_override_amount CHECK (amount >= 0),
    CONSTRAINT chk_override_reason CHECK (reason IN ('TAG_DAMAGED', 'ANTENNA_MALFUNCTION', 'VEHICLE_UNREADABLE', 'MANUAL_GRANT'))
);

CREATE INDEX idx_manual_overrides_override_id ON manual_overrides (override_id);
CREATE INDEX idx_manual_overrides_station_lane ON manual_overrides (station_id, lane_id, registered_at DESC);
CREATE INDEX idx_manual_overrides_operator ON manual_overrides (operator_id, registered_at DESC);
