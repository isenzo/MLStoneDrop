package me.isenzo.mlstonedrop.commands.exceptions.utils;

import me.isenzo.mlstonedrop.commands.exceptions.message.ExceptionMessageProvider;

import java.text.MessageFormat;

public class ValidationUtils {

    public static String getParameterizedExceptionMessage(ExceptionMessageProvider exceptionMessageProvider, Object... parameters) {
        return MessageFormat.format(exceptionMessageProvider.getMessage(), parameters);
    }
}
