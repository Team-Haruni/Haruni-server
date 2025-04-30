package org.haruni.domain.chat.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.entity.ChatType;

@Getter
@Schema(description = "채팅 Response")
public class ChatResponseDto {

    @Schema(
            description = "채팅 유형",
            example = "HARUNI"
    )
    private final ChatType chatType;

    @Schema(
            description = "채팅 본문",
            example= "그렇군요. 본인의 한계에 멈추지 않고 꾸준히 노력하는 모습이 저에게도 큰 귀감으로 다가오네요."
    )
    private final String content;

    @Schema(
            description = "발신 시간",
            example = "11:34"
    )
    private final String sendingTime;

    @Builder
    private ChatResponseDto(ChatType chatType, String content, String sendingTime){
        this.chatType = chatType;
        this.content = content;
        this.sendingTime = sendingTime;
    }

    public static ChatResponseDto entityToDto(Chat chat){
        return ChatResponseDto.builder()
                .chatType(chat.getChatType())
                .content(chat.getContent())
                .sendingTime(chat.getSendingTime())
                .build();
    }
}
