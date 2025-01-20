package org.haruni.domain.haruni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class HaruniService {

    private final HaruniRepository haruniRepository;


    @Transactional(readOnly = true)
    public MainPageResponseDto getHaruni(User user){
        log.info("[HaruniService - getHaruni()] - In");

        Haruni haruni = haruniRepository.findById(user.getHaruni().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        log.info("[HaruniService - getHaruni()] - Load Haruni Succeed");

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        String greetingMessage = getGreetingMessage(now, user.getNickname());

        log.info("[HaruniService - getHaruni()] - Out");

        return MainPageResponseDto.builder()
                .haruniImgUrl(haruni.getHaruniImgUrl())
                .haruniLevelInteger((int)Math.floor(haruni.getLevel()))
                .haruniLevelDecimal(haruni.getLevel() - (int)Math.floor(haruni.getLevel()))
                .greetingMessage(greetingMessage)
                .backgroundImgUrl(user.getBackground().getBackgroundImgUrl())
                .selectedItems(user.getItems())
                .build();
    }

    private String getGreetingMessage(LocalDateTime now, String nickname){
        int hour = now.getHour();

        if (hour < 7) {
            return nickname + "님, 좋은 새벽입니다!";
        } else if (hour < 12) {
            return nickname + "님, 좋은 아침입니다!";
        } else if (hour < 19) {
            return nickname + "님, 즐거운 하루 보내고 계신가요?";
        } else {
            return nickname + "님, 오늘 하루도 수고하셨습니다.";
        }
    }
}
