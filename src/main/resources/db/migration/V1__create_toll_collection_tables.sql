-- Esquema inicial para bounded context toll-collection (HU-001)

CREATE TABLE IF NOT EXISTS lanes (
    lane_id    VARCHAR(50) PRIMARY KEY,
    station_id VARCHAR(50) NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    CONSTRAINT chk_lane_status CHECK (status IN ('OPEN', 'MAINTENANCE', 'CLOSED'))
);

CREATE TABLE IF NOT EXISTS tariff_configs (
    id            VARCHAR(36) PRIMARY KEY,
    station_id    VARCHAR(50) NOT NULL,
    vehicle_class VARCHAR(20) NOT NULL,
    amount        NUMERIC(15, 2) NOT NULL,
    currency      VARCHAR(3) NOT NULL DEFAULT 'COP',
    active        BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_tariff_amount CHECK (amount >= 0),
    CONSTRAINT uq_tariff_station_class_active UNIQUE (station_id, vehicle_class, active)
);

CREATE TABLE IF NOT EXISTS toll_passes (
    pass_id        VARCHAR(100) PRIMARY KEY,
    tag_id         VARCHAR(50) NOT NULL,
    station_id     VARCHAR(50) NOT NULL,
    lane_id        VARCHAR(50) NOT NULL,
    vehicle_class  VARCHAR(20) NOT NULL,
    amount         NUMERIC(15, 2) NOT NULL,
    currency       VARCHAR(3) NOT NULL DEFAULT 'COP',
    status         VARCHAR(20) NOT NULL,
    decline_reason VARCHAR(50),
    detected_at    TIMESTAMPTZ NOT NULL,
    processed_at   TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_pass_status CHECK (status IN ('AUTHORIZED', 'DECLINED')),
    CONSTRAINT chk_pass_amount CHECK (amount >= 0)
);

CREATE INDEX idx_toll_passes_tag_id    ON toll_passes (tag_id);
CREATE INDEX idx_toll_passes_station   ON toll_passes (station_id, detected_at DESC);
CREATE INDEX idx_tariff_configs_lookup ON tariff_configs (station_id, vehicle_class, active);

-- Datos de referencia para desarrollo local
INSERT INTO lanes (lane_id, station_id, status) VALUES
    ('LN-001', 'ST-BOG-001', 'OPEN'),
    ('LN-002', 'ST-BOG-001', 'OPEN')
ON CONFLICT DO NOTHING;

INSERT INTO tariff_configs (id, station_id, vehicle_class, amount, currency, active) VALUES
    ('TC-BOG-001-C1', 'ST-BOG-001', 'CLASS_I',   9500.00, 'COP', TRUE),
    ('TC-BOG-001-C2', 'ST-BOG-001', 'CLASS_II',  12500.00, 'COP', TRUE),
    ('TC-BOG-001-C3', 'ST-BOG-001', 'CLASS_III', 18000.00, 'COP', TRUE)
ON CONFLICT DO NOTHING;
