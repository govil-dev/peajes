# Non-Functional Requirements

> Generado por Guardian Suite (FACTORY-30). Fuente: TLM.

| Atributo | Valor |
| --- | --- |
| security | {'description': 'Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo'} |
| performance | {'description': 'Latencia — autorización cobro: p95 < 150ms; p99 < 300ms end-to-end desde evento Kafka hasta respuesta. Throughput pico: ≥ 1.500 tx/s sostenidas en 5 minutos. Concurrencia: Sin degradación con 500 conexiones R2DBC simultáneas al pool de Postgres.'} |
| scalability | {'description': 'Escale de 0 a 1.500 tx/s en ≤ 60 segundos (Cloud Run autoscaling)'} |
| availability | {'description': '99.95% mensual (≤ 22 min de downtime/mes) para el servicio de cobro'} |
