package org.haruni.domain.haruni.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HaruniInstanceCreateRequestDto {

    // CONCEPT : 모델 서버에 하루니 인스턴스 생성을 요청하는 DTO

    private Long userId;

    private Long haruniId;

    private String prompt;
}