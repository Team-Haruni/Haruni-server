package org.haruni.domain.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Schema(description = "로그인 Response")
@Getter
@NoArgsConstructor
public class TokenResponseDto {

    @Schema(description = "토큰 타입", example = "Bearer")
    private String type;

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    @Builder
    protected TokenResponseDto(String accessToken, String refreshToken){
        this.type = "Bearer";
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
