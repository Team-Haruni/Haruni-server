package org.haruni.domain.user.dto.req;

import lombok.Builder;
import lombok.Getter;
import org.haruni.domain.user.entity.Gender;

@Getter
public class UserSummaryDto {

    private final Long userId;

    private final Gender gender;

    @Builder
    public UserSummaryDto(Long userId, Gender gender) {
        this.userId = userId;
        this.gender = gender;
    }
}
