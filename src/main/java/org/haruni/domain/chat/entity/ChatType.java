package org.haruni.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ChatType {

    HARUNI("HARUNI"),
    USER("USER");

    @JsonValue
    private final String type;

    @JsonCreator
    public static ChatType fromType(String value) {
        return Arrays.stream(values())
                .filter(t -> t.getType().equals(value))
                .findAny()
                .orElse(null);
    }
}