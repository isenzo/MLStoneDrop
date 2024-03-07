package me.isenzo.mlstonedrop.commands.validation.support;

import me.isenzo.mlstonedrop.commands.exceptions.message.ExceptionMessageProvider;

import java.text.MessageFormat;

public class ValidationSupport {

    public static String validateStringWithRegex(ExceptionMessageProvider exceptionMessageProvider, Object... parameters) {
        return MessageFormat.format(exceptionMessageProvider.getMessage(), parameters);
    }
}
