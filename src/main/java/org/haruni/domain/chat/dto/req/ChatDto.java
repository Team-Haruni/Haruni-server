package org.haruni.domain.chat.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatDto {
    private final Long userId;
    private final String content;
    private final String timeStamp;

    @Builder
    private ChatDto(Long userId, String content, String sendingDate, String sendingTime){
        this.userId = userId;
        this.content = content;
        this.timeStamp = sendingDate+sendingTime;
    }
}