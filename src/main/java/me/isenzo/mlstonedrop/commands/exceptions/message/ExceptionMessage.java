package me.isenzo.mlstonedrop.commands.exceptions.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage implements ExceptionMessageProvider {

    INCORRECT_STRING_HEIGHT("Niepoprawna wartość y: {0}, możliwy zakres to y:-63 do y:256");

    private final String message;
}
