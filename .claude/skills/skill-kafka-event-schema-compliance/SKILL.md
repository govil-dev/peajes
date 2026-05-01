---
name: skill-kafka-event-schema-compliance
description: "Ensure all published Kafka events conform to predefined schemas (e.g., Avro, JSON Schema) and include necessary metadata for traceability, compatibility, and reliable consumption by downstream systems."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Kafka Event Schema Compliance

## Objetivo
Ensure all published Kafka events conform to predefined schemas (e.g., Avro, JSON Schema) and include necessary metadata for traceability, compatibility, and reliable consumption by downstream systems.

## Trigger
on code change

## Inputs
- Java source code for event publishing
- Kafka event schemas
- Infrastructure definition

## Procedimiento
1. Identify all Kafka event publishing points in the application (e.g., `ManualOverrideRegistered`, `AccountRecharged`).
2. Verify that event payloads are serialized according to the agreed-upon schema format.
3. Check for the presence of essential metadata fields (e.g., event version, timestamp, source system, correlation ID).
4. Confirm that schema evolution is handled gracefully, ensuring backward and forward compatibility where required.
5. Review the use of Schema Registry if applicable for schema management.

## Output esperado
Report on event schema compliance, identifying any deviations or missing metadata.

## Source refs (project)
- infrastructure_definition:8acb0625-9e4f-4042-9ede-4148f7b9baf2
- use_case:a08d2aa3-7139-4b9b-8347-5c0d3640c290
- use_case:fe4645b8-0c14-47ef-adb1-970bfce30bf7
