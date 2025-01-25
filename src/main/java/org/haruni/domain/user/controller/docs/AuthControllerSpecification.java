package org.haruni.domain.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.oauth.dto.req.OAuthLoginRequestDto;
import org.haruni.domain.user.dto.req.LoginRequestDto;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.haruni.domain.user.dto.res.TokenResponseDto;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "AuthController", description = "Auth management Controller")
public interface AuthControllerSpecification {

    @Operation(summary = "회원가입", description = "회원가입<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 회원가입 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    ),
                                    @ExampleObject(
                                            name = "이메일 중복",
                                            value = "{\"error\" : \"400\", \"message\" : \"이메일 중복 검사에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/sign-up")
    ResponseEntity<ResponseDto<String>> signUp(@Valid @RequestBody SignUpRequestDto request);

    @Operation(summary = "로그인", description = "로그인<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 로그인 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    ResponseEntity<ResponseDto<TokenResponseDto>> login(@Valid@RequestBody LoginRequestDto request);

    @Operation(summary = "소셜 로그인", description = "소셜 로그인<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 소셜 로그인 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 소셜 로그인 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "🚨 소셜 로그인 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "엑세스 토큰 검증 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효하지 않은 인증서버의 엑세스 토큰입니다\"}"
                                    )
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/oauth/login")
    ResponseEntity<?> oauthLogin(@Valid@RequestBody OAuthLoginRequestDto request);
}
