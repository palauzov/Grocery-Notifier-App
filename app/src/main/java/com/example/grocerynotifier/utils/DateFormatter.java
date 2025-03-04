package com.example.grocerynotifier.utils;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    @TypeConverter
    public static String fromDate(Date date) {
        return (date == null) ? null : FORMATTER.format(date);
    }

    @TypeConverter
    public static Date toDate(String dateString) {
        try {
            return (dateString == null) ? null : FORMATTER.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
