package org.haruni.domain.haruni.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "Spring 서버 -> 모델 서버로 하루니 인스턴스 생성 Request")
public class HaruniInstanceCreateRequestDto {

    private final Long userId;
    private final Long haruniId;
    private final String prompt;

    @Builder
    private HaruniInstanceCreateRequestDto(Long userId, Long haruniId, String prompt) {
        this.userId = userId;
        this.haruniId = haruniId;
        this.prompt = prompt;
    }
}