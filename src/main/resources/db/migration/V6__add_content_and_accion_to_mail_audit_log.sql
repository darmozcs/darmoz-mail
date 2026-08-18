ALTER TABLE mail_audit_log ADD COLUMN body_html TEXT;
ALTER TABLE mail_audit_log ADD COLUMN accion VARCHAR(100);

CREATE INDEX idx_mail_audit_log_accion ON mail_audit_log (accion);
