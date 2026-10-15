# Database traceability to the platform specification

This mapping aligns the existing PostgreSQL schema with *System Requirements,
Architecture and Design Specification: Ethiopian Agricultural Marketplace,
Logistics and Payment Platform*, version 2.0 (6 October 2026). Existing
tables are reused; migration `V27` adds missing fields and constraints without
renaming or dropping existing tables.

| Specification area | Database structures |
| --- | --- |
| Identity, roles, temporary credentials, sessions, lockout and audit (IAM-01–15) | `users`, `roles`, `permissions`, `user_roles`, `role_permissions`, `temporary_credentials`, `user_sessions`, `ussd_sessions`, `audit_events`; credentials are stored as hashes with expiry/consumption metadata; session device/channel/activity fields in V27 |
| Farmer, literacy and bank onboarding (FRM-01–20) | `farmers`, `payout_bank_accounts`, `farms`, `farm_locations`, `operator_tasks`, `notifications`; encrypted identity and bank data fields; `literacy_status` and private location visibility |
| Organization registration, documents and consent (ORG-01–14) | `organizations`, `organization_documents`, `documents`, `terms_acceptances`, `operator_tasks` |
| Driver and vehicle verification (DRV-01–11) | `drivers`, `vehicles`, `payout_bank_accounts`, `documents`, `logistics_assignments`; identity, licence, model, colour, owner, registration and verification fields |
| Grain, search, listing and inventory (FRM-12–15, ORD-07) | `farms`, `farm_locations`, `products`, `grain_varieties`, `grain_lots`, `inventory_accounts`, `inventory_transactions`, `inventory_holds`, `listings`; searchable harvest, quality, location and availability attributes |
| Orders and state history (ORD-01–09) | `orders`, `order_items`, `order_state_history`, `idempotency_records`; prepaid amount components, soft-hold expiry and concurrency version |
| Buyer prepayment, holds, refunds, fees and payouts (PAY-01–11) | `payments`, `payment_transactions`, `payment_allocations`, `payment_state_history`, `fee_rules`, `fee_calculations`, `payouts`, `payout_bank_accounts` |
| Double-entry financial records (section 7) | `ledger_accounts`, `journal_entries`, `journal_lines`; V27 enforces balanced debit/credit lines at transaction commit and prevents update/delete |
| Assignment, pickup, GPS and delivery (LOG-01–09) | `logistics_assignments`, `vehicles`, `pickup_records`, `delivery_records`, `trip_location_points`; pickup/delivery code hashes and expiry; location points are assignment-scoped and timestamped, with retention and active-trip access controlled by policy/application |
| Notifications, assistance, compliance and disputes (OPS/CMP) | `notifications`, `notification_deliveries`, `operator_tasks`, `compliance_cases`, `disputes`, `dispute_evidence`, `documents` |
| Idempotency, audit, security and append-only history | `idempotency_records`, `audit_events`, `order_state_history`, `payment_state_history`, `inventory_transactions`, `journal_entries`, `journal_lines` |

## Explicit design boundaries

- Business rules and status transitions remain enforced by backend services;
  schema constraints protect relational integrity, nonnegative values and
  ledger balance.
- Bank and identity values must be encrypted before persistence. V27 adds
  ciphertext and key identifier columns, but does not perform application-key
  dependent encryption of existing values. That needs a separately managed
  data migration and key-handling plan.
- Precise farm locations remain private. Search uses the general listing
  location; application authorization must prevent buyers from reading
  private coordinates.
- Held funds use an authorized provider. These tables record the platform's
  references and accounting state; they do not implement escrow or payment
  processing.
- The specification is pending stakeholder review. Personal Owner onboarding,
  legal retention, fee policy, and several payout/refund decisions remain open
  and must not be inferred from the schema.
