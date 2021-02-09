package com.link8.tw.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTool {

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";


    public static LocalDate getTime(String date, String formatter) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDate time = LocalDate.parse(date, dateFormatter);
        return time;
    }

    public static LocalDateTime getDateTime(String date, String formatter) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(formatter);
        LocalDateTime time = LocalDateTime.parse(date, dateFormatter);
        return time;
    }

    public static String getString(LocalDate date, String formatter) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(formatter);
        String time = date.format(dateFormatter);
        return time;
    }

    public static String getString(LocalDateTime date, String formatter) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(formatter);
        String time = date.format(dateFormatter);
        return time;
    }
}
