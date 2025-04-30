package org.haruni.domain.diary.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.chat.dto.req.ChatDto;
import org.haruni.domain.user.dto.res.UserSummaryDto;

import java.util.List;

@Getter
@Schema(description = "[서비스 서버 to 하루니 서버] 하루 일기 생성 Request")
public class DayDiaryRequestDto {

    private final Long userId;
    private final String gender;
    private final String mbti;
    private final List<ChatDto> chats;

    @Builder
    private DayDiaryRequestDto(UserSummaryDto userSummary, List<ChatDto> chats) {
        this.userId = userSummary.getUserId();
        this.gender = userSummary.getGender().toString();
        this.mbti = userSummary.getMbti().toString();
        this.chats = chats;
    }
}
