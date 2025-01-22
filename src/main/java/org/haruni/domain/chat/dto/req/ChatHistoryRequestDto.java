package org.haruni.domain.chat.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특정일 채팅 내역 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatHistoryRequestDto {

    @Schema(description = "날짜", example = "2025-01-22")
    @NotBlank(message = "날짜는 공백이 될 수 없습니다.")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
            message = "날짜 형식은 YYYY-MM-DD 여야 합니다.")
    private String createdAt;
}
