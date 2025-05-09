package org.haruni.domain.chat.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatResponseBody {

    private final String response;
    private final Long user_id;

    @Builder
    private ChatResponseBody(String response, Long user_id) {
        this.response = response;
        this.user_id = user_id;
    }
}
