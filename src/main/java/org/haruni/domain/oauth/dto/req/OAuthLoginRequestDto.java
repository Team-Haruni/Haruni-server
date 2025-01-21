package org.haruni.domain.oauth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.common.enums.Enum;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;

@Schema(description = "회원가입(소셜로그인) Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthLoginRequestDto {

    @Schema(description = "엑세스 토큰", example = "asdfwqeoiufhasdoiuyfgqoweufasd.ewqfasdfjqwefasdf.qwefadasfqwef")
    @NotBlank(message = "엑세스 토큰은 공백일 수 없습니다.")
    private String accessToken;

    @Schema(description = "소셜 로그인 유형", example = "google/kakao/naver")
    @Enum(target = OAuth2Provider.class, message = "소셜 로그인 타입이 적절하지 않습니다.")
    private String providerId;
}