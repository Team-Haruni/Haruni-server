package org.haruni.domain.model.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.common.util.TimeUtils;
import org.haruni.domain.user.entity.User;

// [Spring to Model] 응답 생성 요청
@Getter
public class HaruniChatRequestDto {

    private final Long userId;
    private final String haruniPersonality;
    private final String mbti;
    private final String gender;
    private final String nickname;
    private final String content;
    private final String sendingDate;
    private final String sendingTime;

    @Builder
    private HaruniChatRequestDto(User user, String haruniPersonality, String content) {
        this.userId = user.getId();
        this.haruniPersonality = haruniPersonality;
        this.mbti = user.getMbti().name();
        this.gender = user.getGender().name();
        this.nickname = user.getNickname();
        this.content = content;
        this.sendingDate = TimeUtils.getCurrentDate();
        this.sendingTime = TimeUtils.getCurrentTime();
    }
}
