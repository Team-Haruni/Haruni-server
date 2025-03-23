package org.haruni.domain.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "로그인(일반) Request")
public class LoginRequestDto {

    @Schema(
            description = "이메일",
            example = "ahh0520@inu.ac.kr"
    )
    @NotBlank(message = "사용자 이메일이 비어있습니다.")
    @Size(max = 50, message = "입력된 이메일의 길이가 너무 깁니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;

    @Schema(
            description = "비밀번호",
            example = "Korean22!"
    )
    @NotBlank(message = "사용자 비밀번호가 비어있습니다.")
    @Size(max = 100, message = "입력된 비밀번호의 길이가 너무 깁니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?])(?=.*[a-z]).{8,15}$")
    private String password;
}
