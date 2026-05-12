# Compliance

> Generado por Guardian Suite (FACTORY-30). Fuente: TLM.

Ley 1581/2012 — Habeas data y protección de datos personales: PII clasificada como dato sensible; almacenamiento cifrado en reposo (AES-256 vía Cloud SQL encryption); logs no contienen PII (enmascaramiento); derecho de supresión (endpoint DELETE /api/v1/accounts/{{userId}} con anonimización); aviso de privacidad y consentimiento explícito; registro de tratamiento de datos ante SIC.
PCI DSS Nivel 4: Aplica para procesamiento de números de tarjeta (recargas PayU); PAN tokenizado (nunca se almacena completo, solo token de PayU); transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3; acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA); escaneo de vulnerabilidades trimestral (ASV externo); log de auditoría para accesos a datos de pago, retención 1 año mínimo.
Resolución DIAN 000042/2020 — Facturación electrónica: Factura electrónica en formato XML UBL 2.1 firmada con certificado digital DIAN; envío a proveedor tecnológico autorizado antes de entregar al adquirente; nota crédito electrónica para anulaciones/correcciones; numeración autorizada con CUFE; retención de XML firmados por 5 años mínimo (sistema retiene 7 años).
SuperTransporte — Reportes regulatorios: Reportes mensuales de pasos por categoría vehicular y estación (out of scope MVP, release 2); arquitectura debe soportar adición de adaptador de reporte sin cambios en el dominio.
