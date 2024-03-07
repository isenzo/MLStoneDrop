package me.isenzo.mlstonedrop.commands.exceptions;

import me.isenzo.mlstonedrop.commands.exceptions.message.ExceptionMessageProvider;

public class DataViolanteException extends RuntimeException {

    public DataViolanteException(String message) {
        super(message);
    }

    public DataViolanteException(ExceptionMessageProvider exceptionMessageProvider) {
        super(exceptionMessageProvider.getMessage());
    }

    public DataViolanteException(ExceptionMessageProvider exceptionMessageProvider, String messageParam) {
        super(exceptionMessageProvider.getMessage() + messageParam);
    }
}
