package org.haruni.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE");

    @JsonValue
    private final String gender;

    @JsonCreator
    public static Gender fromGender(String value){
        return Arrays.stream(values())
                .filter(g -> g.getGender().equals(value))
                .findAny()
                .orElse(null);
    }

}
