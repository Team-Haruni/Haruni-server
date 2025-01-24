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

    @Schema(
            description = "채팅 본문",
            example = "오늘은 한강 잠실교에서 러닝을 뛰고 왔어. 숨이 정말 가빠졌는데 포기하고 싶지 않았어. 정말 뿌듯하더라고!"
    )
    @NotBlank(message = "채팅 본문이 비어있습니다")
    @Size(max = 300, message = "채팅 본문의 길이가 너무 깁니다")
    private String content;

}