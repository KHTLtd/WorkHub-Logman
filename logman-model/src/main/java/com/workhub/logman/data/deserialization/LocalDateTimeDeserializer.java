package com.workhub.logman.data.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    private static final Logger log = LoggerFactory.getLogger(LocalDateTimeDeserializer.class);

    public LocalDateTimeDeserializer() {
        super((JavaType) null);
    }

    public LocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext) {
        LocalDateTime dateTime = null;
        try {
            long mills = Long.parseLong(parser.getText());
            Instant instant = Instant.ofEpochMilli(mills);
            dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        } catch (IOException e) {
            log.error("Failed to deserialize json string to LocalDateTime : " + e.getMessage(), e);
        }
        return dateTime;
    }
}
