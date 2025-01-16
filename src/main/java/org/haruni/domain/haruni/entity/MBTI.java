package org.haruni.domain.haruni.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MBTI {

    INTJ("INTJ"), INTP("INTP"), INFJ("INFJ"), INFP("INFP"),
    ISTJ("ISTJ"), ISTP("ISTP"), ISFJ("ISFJ"), ISFP("ISFP"),
    ENTJ("ENTJ"), ENTP("ENTP"), ENFJ("ENFJ"), ENFP("ENFP"),
    ESTJ("ESTJ"), ESTP("ESTP"), ESFJ("ESFJ"), ESFP("ESFP");

    @JsonValue private final String mbti;

    @JsonCreator
    public static MBTI fromMBTI(String value){
        return Arrays.stream(values())
                .filter(m -> m.getMbti().equals(value))
                .findAny()
                .orElse(null);
    }
}