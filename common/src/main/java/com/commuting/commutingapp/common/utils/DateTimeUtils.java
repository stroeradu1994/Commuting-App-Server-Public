package com.commuting.commutingapp.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils {

    public static Instant getInstantFromDateTimeString(String dateTime) throws ParseException {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return df1.parse(dateTime).toInstant();
    }

    public static LocalDateTime getTimeNow() {
        return LocalDateTime.now(ZoneId.of("Europe/Bucharest"));
    }
}
