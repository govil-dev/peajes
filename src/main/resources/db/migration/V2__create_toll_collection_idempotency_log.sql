-- Registro de idempotencia para lookups rápidos
CREATE TABLE tc_processed_passes (
    pass_id         VARCHAR(64) PRIMARY KEY,
    processed_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    result          VARCHAR(16) NOT NULL
);

CREATE INDEX idx_tc_processed_passes_time ON tc_processed_passes(processed_at DESC);
