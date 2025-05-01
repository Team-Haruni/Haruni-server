package org.haruni.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.common.enums.Enum;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.user.entity.Gender;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원가입(일반) Request")
public class SignUpRequestDto {

    @Schema(
            description = "이메일",
            example = "ahh0520@inu.ac.kr"
    )
    @NotBlank(message = "사용자 이메일이 비어있습니다")
    @Size(max = 50, message = "사용자 이메일의 길이가 너무 깁니다")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;

    @Schema(
            description = "비밀번호",
            example = "Korean22!"
    )
    @Size(max = 100, message = "사용자 비밀번호의 길이가 너무 깁니다")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?])(?=.*[a-z]).{8,15}$")
    private String password;

    @Schema(
            description = "사용자 유형",
            example = "NORMAL/GOOGLE/KAKAO/NAVER"
    )
    @Enum(target = OAuth2Provider.class, message = "유저 타입이 옳바르지 않습니다")
    private String providerId;

    @Schema(
            description = "성별",
            example = "MALE/FEMALE"
    )
    @Enum(target = Gender.class, message = "유저 성별이 옳바르지 않습니다")
    private String gender;

    @Schema(
            description = "닉네임",
            example = "준소이"
    )
    @NotBlank(message = "사용자 닉네임이 비어있습니다")
    @Size(max = 50, message = "사용자 닉네임의 길이가 너무 깁니다.")
    private String nickname;

    @Schema(
            description = "알람 허용 여부",
            example = "true"
    )
    @NotNull(message = "알람 허용 여부가 비어있습니다")
    private Boolean alarmActive;

    @Schema(
            description = "알람 전송 시간",
            example = "11:10"
    )
    @NotBlank(message = "알람 전송 시간이 비어있습니다")
    private String alarmActiveTime;

    @Schema(
            description = "FCM 토큰",
            example = "asdjfhqiuefhadsfbgqweyugfgfjgkn"
    )
    @NotBlank(message = "FCM 토큰이 비어있습니다")
    private String fcmToken;

    @Schema(
            description = "하루니 이름",
            example = "인절미 빙수"
    )
    @NotBlank(message = "하루니 이름이 비어있습니다")
    @Size(max = 50, message = "하루니 이름의 길이가 너무 깁니다")
    private String haruniName;

    @Schema(
            description = "MBTI",
            example = "ENTJ"
    )
    @NotBlank(message = "MBTI가 비어있습니다")
    private String mbti;
}