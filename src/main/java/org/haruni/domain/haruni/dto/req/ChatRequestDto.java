package org.haruni.domain.haruni.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "채팅 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRequestDto {

    @Schema(description = "채팅 내용", example = "오늘은 러닝을 뛰고 왔어! 정말 상쾌하더라")
    @NotBlank(message = "채팅 내용은 공백일 수 없습니다.")
    @Size(max = 300, message = "채팅 내용의 길이가 너무 깁니다.(최대 300자)")
    private String content;
}
