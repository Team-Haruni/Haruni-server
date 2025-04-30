package org.haruni.domain.chat.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.user.entity.User;

@Getter
public class ChatRequestBody {

    private final Long userId;
    private final String mbti;
    private final String gender;
    private final String content;

    @Builder
    private ChatRequestBody(User user, String content) {
        this.userId = user.getId();
        this.mbti = user.getMbti().name();
        this.gender = user.getGender().name();
        this.content = content;
    }
}
