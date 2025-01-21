package org.haruni.domain.haruni.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "프롬프트 수정 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PromptUpdateRequestDto {

    @Schema(description = "하루니 프롬프트", example = "저는 예술에 조예가 깊은 편입니다~")
    @NotBlank(message = "하루니 프롬프트가 비어있습니다.")
    @Size(max = 65535, message = "프롬프트의 길이가 너무 깁니다.")
    private String prompt;
}
