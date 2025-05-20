package org.haruni.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "알람 전송 시간 수정 Request")
public class AlarmActiveTimeUpdateRequestDto {

    @Schema(
            description = "알람 전송 시간",
            example = "11:10"
    )
    @NotBlank(message = "알람 전송 시간이 비어있습니다")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "알람 시간은 HH:MM 형식이어야 합니다")
    private String alarmActiveTime;
}
