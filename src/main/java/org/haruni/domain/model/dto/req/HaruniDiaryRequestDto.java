package org.haruni.domain.model.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.chat.dto.req.ChatDto;
import org.haruni.domain.user.dto.res.UserSummaryDto;

import java.util.List;

// [Spring to Model] 다이어리 생성 요청
@Getter
public class HaruniDiaryRequestDto {

    private final Long userId;
    private final String gender;
    private final String mbti;
    private final List<ChatDto> chats;

    @Builder
    private HaruniDiaryRequestDto(UserSummaryDto userSummary, List<ChatDto> chats) {
        this.userId = userSummary.getUserId();
        this.gender = userSummary.getGender().toString();
        this.mbti = userSummary.getMbti().toString();
        this.chats = chats;
    }
}
