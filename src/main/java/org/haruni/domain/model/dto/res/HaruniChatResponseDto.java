package org.haruni.domain.model.dto.res;

import lombok.Builder;
import lombok.Getter;

// [Model to Spring] 채팅 전송
@Getter
public class HaruniChatResponseDto {

    private final String response;
    private final Long user_id;

    @Builder
    private HaruniChatResponseDto(String response, Long user_id) {
        this.response = response;
        this.user_id = user_id;
    }
}
