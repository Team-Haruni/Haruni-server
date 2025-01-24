package org.haruni.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    private String alarmActiveTime;
}
