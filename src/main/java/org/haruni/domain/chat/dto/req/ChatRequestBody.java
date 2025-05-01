package org.haruni.domain.chat.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.common.util.TimeUtils;
import org.haruni.domain.user.entity.User;

@Getter
public class ChatRequestBody {

    private final Long userId;
    private final String mbti;
    private final String gender;
    private final String content;
    private final String sendingDate;
    private final String sendingTime;

    @Builder
    private ChatRequestBody(User user, String content) {
        this.userId = user.getId();
        this.mbti = user.getMbti().name();
        this.gender = user.getGender().name();
        this.content = content;
        this.sendingDate = TimeUtils.getCurrentDate();
        this.sendingTime = TimeUtils.getCurrentTime();
    }
}
