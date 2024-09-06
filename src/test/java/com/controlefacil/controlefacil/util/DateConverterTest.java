package com.controlefacil.controlefacil.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class DateConverterTest {

    @Test
    public void testParseDateValid() {
        String dateString = "06/09/2024";
        LocalDate expectedDate = LocalDate.of(2024, 9, 6);

        LocalDate result = DateConverter.parseDate(dateString);

        assertEquals(expectedDate, result);
    }

    @Test
    public void testParseDateInvalidFormat() {
        String dateString = "2024-09-06";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            DateConverter.parseDate(dateString);
        });

        assertEquals("Data no formato inválido. Use dd/MM/yyyy.", thrown.getMessage());
    }

    @Test
    public void testParseDateNull() {
        String dateString = null;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            DateConverter.parseDate(dateString);
        });

        assertEquals("Data no formato inválido. Use dd/MM/yyyy.", thrown.getMessage());
    }

    @Test
    public void testFormatDate() {
        LocalDate date = LocalDate.of(2024, 9, 6);
        String expectedDateString = "06/09/2024";

        String result = DateConverter.formatDate(date);

        assertEquals(expectedDateString, result);
    }
}
