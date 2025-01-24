package org.haruni.domain.chat.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.haruni.domain.chat.entity.Chat;

@Getter
@Schema(description = "채팅 Response")
public class ChatResponseDto {

    @Schema(
            description = "발신자 닉네임",
            example = "하룬이"
    )
    private final String senderName;

    @Schema(
            description = "채팅 본문",
            example= "그렇군요. 본인의 한계에 멈추지 않고 꾸준히 노력하는 모습이 저에게도 큰 귀감으로 다가오네요."
    )
    private final String content;

    @Schema(
            description = "발신 시간",
            example = "11:34"
    )
    private final String createdAt;

    @Builder
    private ChatResponseDto(String senderName, String content, String createdAt){
        this.senderName = senderName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ChatResponseDto entityToDto(Chat chat){
        return ChatResponseDto.builder()
                .senderName(chat.getSenderName())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
