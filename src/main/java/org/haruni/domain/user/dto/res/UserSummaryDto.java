package org.haruni.domain.user.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.haruni.entity.MBTI;
import org.haruni.domain.user.entity.Gender;

@Getter
public class UserSummaryDto {

    private final Long userId;

    private final Gender gender;

    private final MBTI mbti;

    @Builder
    private UserSummaryDto(Long userId, Gender gender, MBTI mbti) {
        this.userId = userId;
        this.gender = gender;
        this.mbti = mbti;
    }
}
