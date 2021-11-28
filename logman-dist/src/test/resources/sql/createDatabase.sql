CREATE SCHEMA logman_schema;

CREATE TABLE logman_schema.log (
    log_id character varying(99) PRIMARY KEY,
    lm_host character varying(99),
    lm_address character varying(99),
    insert_stamp timestamp(2) without time zone,
    subsystem character varying(99),
    logger character varying(99),
    sub_host character varying(99),
    sub_address character varying(99),
    create_stamp timestamp(2) without time zone,
    ex character varying(99),
    message character varying(99),
);