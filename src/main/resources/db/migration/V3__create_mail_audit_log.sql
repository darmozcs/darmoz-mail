CREATE TABLE mail_audit_log (
    id BIGSERIAL PRIMARY KEY,
    recipient VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    sent_at TIMESTAMPTZ NOT NULL,
    client_id UUID NOT NULL,
    scheduled_email_id BIGINT NULL REFERENCES mail_scheduled_email(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_mail_audit_log_client_id ON mail_audit_log (client_id);
CREATE INDEX idx_mail_audit_log_recipient ON mail_audit_log (recipient);
