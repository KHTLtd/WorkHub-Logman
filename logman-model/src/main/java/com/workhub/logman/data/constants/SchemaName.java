package com.workhub.logman.data.constants;

import lombok.Getter;

public enum SchemaName {
    LOG("log_schema"),
    PG_PATH("ext");

    @Getter
    private String schema;

    SchemaName(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return schema;
    }
}
