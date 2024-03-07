package me.isenzo.mlstonedrop.commands.validation.regexpattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegexValidation {
    HEIGHT_PATTERN("^-?[0-9]{1,3}$");

    private final String pattern;
}
