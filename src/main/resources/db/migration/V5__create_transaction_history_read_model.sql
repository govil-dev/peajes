CREATE TABLE tc_transaction_history_rm (
    id              UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id  VARCHAR(100) NOT NULL UNIQUE,
    account_id      VARCHAR(36)  NOT NULL,
    station_name    VARCHAR(200) NOT NULL,
    amount          NUMERIC(15,2) NOT NULL,
    currency        VARCHAR(3)   NOT NULL DEFAULT 'COP',
    vehicle_class   VARCHAR(50)  NOT NULL,
    authorized_at   TIMESTAMPTZ  NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_tc_history_account_date
    ON tc_transaction_history_rm (account_id, authorized_at DESC);

CREATE INDEX idx_tc_history_transaction_id
    ON tc_transaction_history_rm (transaction_id);
