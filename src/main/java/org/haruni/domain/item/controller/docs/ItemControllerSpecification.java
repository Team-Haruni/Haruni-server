package org.haruni.domain.item.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.item.dto.req.ItemSaveRequestDto;
import org.haruni.domain.item.dto.res.SelectedItemResponseDto;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "ItemController", description = "Item management Controller")
public interface ItemControllerSpecification {

    @Operation(summary = "선택된 아이탬 조회", description = "선택된 아이템을 조회합니다<br>" +
                                                         "🔐 <strong>Jwt 필요</strong><br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 아이템 조회 성공"),
            @ApiResponse(responseCode = "404", description = "🚨 아이템 조회 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),

                            },
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    ResponseEntity<ResponseDto<List<SelectedItemResponseDto>>> getSelectedItem(@AuthenticationPrincipal UserDetailsImpl user);

    @Operation(summary = "선택 아이탬 수정", description = "선택 아이탬 수정<br>" +
                                                       "🔐 <strong>Jwt 필요</strong><br>" +
                                                       "🧪 선택된 아이탬이 A, C, B -> A, B, C로 교체되어도 해당 API를 사용하셔야 합니다<br>" +
                                                       "   새로운 아이탬을 추가하는 경우에도 동일")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "✅ 아이템 수정 성공"),
            @ApiResponse(responseCode = "400", description = "🚨 아이템 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유효성 검사 실패",
                                            value = "{\"error\" : \"400\", \"message\" : \"유효성 검사에 실패하였습니다\"}"
                                    ),
                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "🚨 아이템 수정 실패",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "유저 조회 실패",
                                            value = "{\"error\" : \"404\", \"message\" : \"유저 조회에 실패하였습니다\"}"
                                    ),

                            },
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    ResponseEntity<ResponseDto<Boolean>> saveItems(@AuthenticationPrincipal UserDetailsImpl user,
                                                   @Valid @RequestBody ItemSaveRequestDto request);
}