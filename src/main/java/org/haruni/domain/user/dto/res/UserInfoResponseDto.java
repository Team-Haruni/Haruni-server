package org.haruni.domain.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저 정보 조회 Response")
@Getter
@NoArgsConstructor
public class UserInfoResponseDto {

    @Schema(description = "유저 닉네임", example = "삼준서")
    private String nickname;

    @Schema(description = "유저 이메일", example = "ahh0520@inu.ac.kr")
    private String email;

    @Schema(description = "유저 이메일 수정 가능 여부", example = "true/false(소셜로그인 사용자일 경우 수정 불가능)")
    private Boolean emailUpdateAvailable;

    @Builder
    protected UserInfoResponseDto(String nickname, String email, Boolean emailUpdateAvailable){
        this.nickname = nickname;
        this.email = email;
        this.emailUpdateAvailable = emailUpdateAvailable;
    }

}
