INSERT INTO mail_email_template (code, name, subject, body_html, body_text, active)
VALUES (
    'VERIFY',
    'Verificacion de correo',
    'Tu codigo de verificacion',
    '<p>Tu codigo de verificacion es: <strong>${code}</strong></p><p>Si no solicitaste este codigo, ignora este mensaje.</p>',
    'Tu codigo de verificacion es: ${code}. Si no solicitaste este codigo, ignora este mensaje.',
    TRUE
);
