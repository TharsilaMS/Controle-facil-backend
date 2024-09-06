package com.controlefacil.controlefacil.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate parseDate(String dateString) {
        if (dateString == null) {
            throw new IllegalArgumentException("Data no formato inválido. Use dd/MM/yyyy.");
        }
        try {
            return LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data no formato inválido. Use dd/MM/yyyy.");
        }
    }

    public static String formatDate(LocalDate date) {
        return date.format(FORMATTER);
    }
}
