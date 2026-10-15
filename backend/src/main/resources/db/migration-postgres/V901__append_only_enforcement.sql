-- PostgreSQL-only enforcement objects moved out of the portable baseline.

CREATE OR REPLACE FUNCTION marketplace.reject_immutable_record_change()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Records in %.% are append-only', TG_TABLE_SCHEMA, TG_TABLE_NAME;
END;
$$;

CREATE OR REPLACE FUNCTION marketplace.assert_journal_entry_balanced()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE
    entry_id_to_check BIGINT;
    debit_total NUMERIC(19, 2);
    credit_total NUMERIC(19, 2);
    line_count BIGINT;
BEGIN
    entry_id_to_check := COALESCE(NEW.entry_id, OLD.entry_id);
    SELECT COUNT(*),
           COALESCE(SUM(amount) FILTER (WHERE direction = 'DEBIT'), 0),
           COALESCE(SUM(amount) FILTER (WHERE direction = 'CREDIT'), 0)
      INTO line_count, debit_total, credit_total
      FROM marketplace.journal_lines
     WHERE entry_id = entry_id_to_check;
    IF line_count < 2 OR debit_total <> credit_total THEN
        RAISE EXCEPTION 'Journal entry % must contain balanced debit and credit lines', entry_id_to_check;
    END IF;
    RETURN NULL;
END;
$$;

CREATE CONSTRAINT TRIGGER trg_journal_lines_balanced
AFTER INSERT OR UPDATE OR DELETE ON marketplace.journal_lines
DEFERRABLE INITIALLY DEFERRED
FOR EACH ROW EXECUTE FUNCTION marketplace.assert_journal_entry_balanced();

CREATE TRIGGER trg_journal_entries_append_only
BEFORE UPDATE OR DELETE ON marketplace.journal_entries
FOR EACH ROW EXECUTE FUNCTION marketplace.reject_immutable_record_change();
CREATE TRIGGER trg_journal_lines_append_only
BEFORE UPDATE OR DELETE ON marketplace.journal_lines
FOR EACH ROW EXECUTE FUNCTION marketplace.reject_immutable_record_change();
CREATE TRIGGER trg_inventory_transactions_append_only
BEFORE UPDATE OR DELETE ON marketplace.inventory_transactions
FOR EACH ROW EXECUTE FUNCTION marketplace.reject_immutable_record_change();
CREATE TRIGGER trg_audit_events_append_only
BEFORE UPDATE OR DELETE ON marketplace.audit_events
FOR EACH ROW EXECUTE FUNCTION marketplace.reject_immutable_record_change();
CREATE TRIGGER trg_payment_state_history_append_only
BEFORE UPDATE OR DELETE ON marketplace.payment_state_history
FOR EACH ROW EXECUTE FUNCTION marketplace.reject_immutable_record_change();

