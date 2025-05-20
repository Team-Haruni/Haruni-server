package org.haruni.domain.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "유저 정보 조회 Response")
public class UserInfoResponseDto {

    @Schema(
            description = "유저 닉네임",
            example = "삼준서"
    )
    private final String userNickname;

    @Schema(
            description = "하루니 닉네임",
            example = "설빙"
    )
    private final String haruniNickname;

    @Schema(
            description = "유저 이메일",
            example = "ahh0520@inu.ac.kr"
    )
    private final String email;

    @Schema(
            description = "유저 이메일 수정 가능 여부",
            example = "true/false(소셜로그인 사용자일 경우 수정 불가능)"
    )
    private final Boolean emailUpdateAvailable;

    @Builder
    private UserInfoResponseDto(String userNickname, String haruniNickname, String email, Boolean emailUpdateAvailable){
        this.userNickname = userNickname;
        this.haruniNickname = haruniNickname;
        this.email = email;
        this.emailUpdateAvailable = emailUpdateAvailable;
    }
}
