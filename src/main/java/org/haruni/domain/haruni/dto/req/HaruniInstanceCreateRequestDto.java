package org.haruni.domain.haruni.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HaruniInstanceCreateRequestDto {
    private Long userId;
    private Long haruniId;
    private String prompt;
}
