package org.haruni.domain.chat.dto.req;

import lombok.*;

@Getter
public class ChatRequestBody {

    private final Long haruniId;
    private final String content;

    @Builder
    private ChatRequestBody(Long haruniId, String content) {
        this.haruniId = haruniId;
        this.content = content;
    }
}
