package org.haruni.domain.chat.dto.req;

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

    // CONCEPT : 하루니 모델 서버에 전송할 채팅 양식(Client -> Spring)

    @Schema(description = "채팅 내용", example = "오늘은 러닝을 뛰고 왔어! 정말 상쾌하더라")
    @NotBlank(message = "채팅 내용은 공백일 수 없습니다.")
    @Size(max = 300, message = "채팅 내용의 길이가 너무 깁니다.(최대 300자)")
    private String content;

}