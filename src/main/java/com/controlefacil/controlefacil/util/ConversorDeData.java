package com.controlefacil.controlefacil.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitária para conversão entre Strings e objetos LocalDate.
 * Facilita o trabalho com datas no formato "dd/MM/yyyy".
 */
public class ConversorDeData {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Converte uma String no formato "dd/MM/yyyy" para um objeto LocalDate.
     *
     * @param dateString A String representando a data a ser convertida.
     * @return O objeto LocalDate correspondente à String fornecida.
     * @throws IllegalArgumentException Se a String for nula ou não estiver no formato esperado.
     */
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

    /**
     * Formata um objeto LocalDate em uma String no formato "dd/MM/yyyy".
     *
     * @param date O objeto LocalDate a ser formatado.
     * @return A String representando a data no formato "dd/MM/yyyy".
     */
    public static String formatDate(LocalDate date) {
        return date.format(FORMATTER);
    }
}
