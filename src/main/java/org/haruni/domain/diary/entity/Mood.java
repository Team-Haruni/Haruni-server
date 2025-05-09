package org.haruni.domain.diary.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Mood {
    HAPPY("happy"),
    SAD("sad"),
    NORMAL("normal");

    @JsonValue
    private final String emotion;

    @JsonCreator
    public static Mood fromEmotion(String value) {
        return Arrays.stream(values())
                .filter(m -> m.getEmotion().equals(value))
                .findAny()
                .orElse(null);
    }
}
