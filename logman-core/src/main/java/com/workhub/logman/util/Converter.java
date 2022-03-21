package com.workhub.logman.util;

import com.workhub.logman.data.LogData;
import org.apache.commons.lang3.StringUtils;

public final class Converter {

    private static final int MAX_TEXT_COLUMN_SIZE = 99;

    private Converter() {
    }

    public static void trimDataForInsertion(LogData in) {
        in.setLogger(trim(in.getLogger()));
        in.setEx(trim(in.getEx()));
        in.setMessage(trim(in.getMessage()));
        in.setSubAddress(trim(in.getSubAddress()));
        in.setDistrSubsystem(trim(in.getDistrSubsystem()));
        in.setSubHost(trim(in.getSubHost()));
    }

    private static String trim(String in) {
        return trim(in, MAX_TEXT_COLUMN_SIZE);
    }

    private static String trim(String in, int len) {
        if (StringUtils.isBlank(in)) {
            return in;
        }
        if (in.length() > len) {
            return in.substring(0, len);
        }
        return in;
    }
}
