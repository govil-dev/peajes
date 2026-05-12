-- Tablas para HU-003: Recargar saldo de cuenta prepago vía PSE

CREATE TABLE IF NOT EXISTS prepaid_accounts (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id  UUID NOT NULL UNIQUE,
    balance     NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    currency    VARCHAR(3)  NOT NULL DEFAULT 'COP',
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT chk_prepaid_balance_non_negative CHECK (balance >= 0),
    CONSTRAINT chk_prepaid_currency CHECK (currency = 'COP'),
    CONSTRAINT chk_prepaid_status CHECK (status IN ('ACTIVE', 'FROZEN', 'SUSPENDED'))
);

CREATE TABLE IF NOT EXISTS recharge_transactions (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id              UUID        NOT NULL,
    external_reference_id   VARCHAR(100) NOT NULL UNIQUE,
    amount                  NUMERIC(15, 2) NOT NULL,
    currency                VARCHAR(3)  NOT NULL DEFAULT 'COP',
    status                  VARCHAR(20) NOT NULL,
    failure_reason          VARCHAR(100),
    created_at              TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_recharge_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_recharge_currency CHECK (currency = 'COP'),
    CONSTRAINT chk_recharge_status CHECK (status IN ('COMPLETED', 'FAILED')),
    CONSTRAINT fk_recharge_account FOREIGN KEY (account_id) REFERENCES prepaid_accounts(account_id)
);

CREATE INDEX idx_recharge_external_ref ON recharge_transactions (external_reference_id);
CREATE INDEX idx_recharge_account_id   ON recharge_transactions (account_id, created_at DESC);
