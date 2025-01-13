package org.haruni.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.common.enums.Enum;
import org.haruni.domain.haruni.entity.MBTI;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Schema(description = "회원가입(일반) Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {

    @Schema(description = "이메일", example = "ahh0520@inu.ac.kr")
    @NotBlank(message = "사용자 이메일이 비어있습니다.")
    @Size(max = 50, message = "사용자 이메일의 길이가 너무 깁니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;

    @Schema(description = "비밀번호", example = "Korean22!")
    @NotBlank(message = "사용자 비밀번호가 비어있습니다.")
    @Size(max = 100, message = "사용자 비밀번호의 길이가 너무 깁니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?])(?=.*[a-z]).{8,15}$")
    private String password;


    @Schema(description = "닉네임", example = "준소이")
    @NotBlank(message = "사용자 닉네임이 비어있습니다.")
    @Size(max = 50, message = "사용자 닉네임의 길이가 너무 깁니다.")
    private String nickname;

    @Schema(description = "알람 허용 여부", example = "true")
    @NotNull(message = "알람 허용 여부가 비어있습니다.")
    private Boolean alarmActive;

    @Schema(description = "알람 전송 시간", example = "11:23")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime alarmActiveTime;

    @Schema(description = "하루니 이름", example = "인절미 빙수")
    @NotBlank(message = "하루니 이름이 비어있습니다.")
    @Size(max = 50, message = "하루니 이름의 길이가 너무 깁니다.")
    private String haruniName;

    @Schema(description = "하루니 MBTI", example = "INTJ")
    @Enum(target = MBTI.class, message = "MBTI 타입이 옳바르지 않습니다.")
    private String mbti;

    @Schema(description = "하루니 프롬프트", example = "저는 예술에 조예가 깊은 편입니다~")
    @NotBlank(message = "하루니 프롬프트가 비어있습니다.")
    @Size(max = 65535, message = "프롬프트의 길이가 너무 깁니다.")
    private String prompt;
}