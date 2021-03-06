package com.salon.booking.utility;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.OptionalInt;
import java.util.function.Supplier;

public final class ParseUtility {

    private ParseUtility() {

    }

    public static long parseLongOrDefault(String number, long defaultValue) {
        if (number == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static LocalDate parseLocalDateOrDefault(String date, LocalDate defaultDate) {
        if (date == null) {
            return defaultDate;
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return defaultDate;
        }
    }

    public static OptionalInt parseInt(String number) {
        if (number == null) {
            return OptionalInt.empty();
        }

        try {
            return OptionalInt.of(Integer.parseInt(number));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    static int parseIntOrThrow(String value, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (value == null) {
            throw exceptionSupplier.get();
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw exceptionSupplier.get();
        }
    }
}
