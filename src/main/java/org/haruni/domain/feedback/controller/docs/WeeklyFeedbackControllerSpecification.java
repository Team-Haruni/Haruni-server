package org.haruni.domain.feedback.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.feedback.dto.res.WeeklyFeedbackResponseDto;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "WeeklyFeedbackController", description = "WeeklyFeedback management Controller")
public interface WeeklyFeedbackControllerSpecification {

    @Operation(summary = "주간 피드백 조회", description = "주간 피드백을 조회합니다.<br>" +
            "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 주간 피드백 조회 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 주간 피드백 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "주간 피드백 조회 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"주간 피드백이 아직 생성되지 않았습니다.\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    ResponseEntity<ResponseDto<WeeklyFeedbackResponseDto>> getFeedback(@AuthenticationPrincipal UserDetailsImpl user);
}
