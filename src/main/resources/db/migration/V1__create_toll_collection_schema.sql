-- Tabla principal de pasos de peaje
CREATE TABLE tc_toll_passes (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    pass_id         VARCHAR(64) NOT NULL UNIQUE,
    tag_id          VARCHAR(16) NOT NULL,
    station_id      UUID        NOT NULL,
    lane_id         UUID        NOT NULL,
    vehicle_class   VARCHAR(16) NOT NULL,
    tariff_amount   NUMERIC(12, 2) NOT NULL,
    currency        VARCHAR(3)  NOT NULL DEFAULT 'COP',
    status          VARCHAR(16) NOT NULL,
    decline_reason  VARCHAR(32),
    detected_at     TIMESTAMPTZ NOT NULL,
    processed_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_tariff_amount_positive CHECK (tariff_amount >= 0)
);

CREATE INDEX idx_tc_toll_passes_pass_id    ON tc_toll_passes(pass_id);
CREATE INDEX idx_tc_toll_passes_tag_id     ON tc_toll_passes(tag_id);
CREATE INDEX idx_tc_toll_passes_station    ON tc_toll_passes(station_id, detected_at DESC);

-- Configuración de carriles
CREATE TABLE tc_lanes (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    station_id  UUID        NOT NULL,
    lane_code   VARCHAR(16) NOT NULL,
    status      VARCHAR(16) NOT NULL DEFAULT 'OPEN',
    CONSTRAINT uq_tc_lanes_station_code UNIQUE (station_id, lane_code)
);

-- Configuración de tarifas vigentes
CREATE TABLE tc_tariff_configs (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    station_id      UUID            NOT NULL,
    vehicle_class   VARCHAR(16)     NOT NULL,
    amount          NUMERIC(12, 2)  NOT NULL CHECK (amount > 0),
    currency        VARCHAR(3)      NOT NULL DEFAULT 'COP',
    valid_from      DATE            NOT NULL,
    valid_until     DATE,
    active          BOOLEAN         NOT NULL DEFAULT true,
    CONSTRAINT uq_tc_tariff_station_class_from UNIQUE (station_id, vehicle_class, valid_from)
);

CREATE INDEX idx_tc_tariff_active ON tc_tariff_configs(station_id, vehicle_class) WHERE active = true;
