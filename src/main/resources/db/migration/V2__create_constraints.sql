ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_amount_positive CHECK (amount > 0);

ALTER TABLE
    ADD CONSTRAINT chk_different_user CHECK (payer_id <> payee_id);
