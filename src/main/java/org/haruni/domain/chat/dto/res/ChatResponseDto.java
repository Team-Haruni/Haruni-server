package org.haruni.domain.chat.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.chat.entity.Chat;

@Schema(description = "채팅 Response")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {

    // CONCEPT : 클라이언트에게 반환할 채팅 양식

    @Schema(description = "송신자 닉네임", example = "하룬이")
    private String senderName;

    @Schema(description = "채팅 내용", example= "그렇군요. 정말 굉장한 하루네요!")
    private String content;

    @Schema(description = "답변 시간", example = "11:34")
    private String createdAt;

    public static ChatResponseDto from(Chat chat){
        return ChatResponseDto.builder()
                .senderName(chat.getSenderName())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
