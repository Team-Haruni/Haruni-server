package org.haruni.domain.haruni.service;

import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.chat.dto.req.ChatRequestBodyDto;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.service.ChatService;
import org.haruni.domain.haruni.dto.req.ChatRequestDto;
import org.haruni.domain.haruni.dto.req.PromptUpdateRequestDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
public class HaruniService {

    private final HaruniRepository haruniRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final RestTemplate modelServerTemplate;

    public HaruniService(HaruniRepository haruniRepository, ChatService chatService, UserRepository userRepository, @Qualifier("modelServerTemplate") RestTemplate modelServerTemplate) {
        this.haruniRepository = haruniRepository;
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.modelServerTemplate = modelServerTemplate;
    }

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

    @Transactional
    public String updatePrompt(User user, PromptUpdateRequestDto request){
        log.info("[HaruniService - updatePrompt()] - In");

        Haruni haruni = haruniRepository.findById(user.getHaruni().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        haruni.updatePrompt(request.getPrompt());

        log.info("[HaruniService - updatePrompt()] - Out");

        return request.getPrompt();
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

    @Transactional
    public Chat processMessage(User authUser, ChatRequestDto request){
        /**
         * 채팅룸 조회, 사용자 채팅 저장
         * 모델에게 전송
         * 요청 (채팅, 모델 고유 아이디) 응답(답변)
         * 응답 저장, 응답 반환
         */
        User user = userRepository.findByEmail(authUser.getEmail())
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        chatService.saveUserChat(user, request);

        Haruni haruni = haruniRepository.findById(authUser.getHaruni().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        ChatRequestBodyDto requestBody = ChatRequestBodyDto.builder()
                .haruniId(haruni.getId())
                .content(request.getContent())
                .build();

        String responseBody;
        try{
            responseBody = modelServerTemplate.postForObject(
                    "/chat",
                    requestBody,
                    String.class
            );
        }catch (HttpClientErrorException e){
            throw new RestApiException(CustomErrorCode.POST_MESSAGE_TO_MODEL_SERVER_FAILED);
        }

        return chatService.saveHaruniChat(user, responseBody);
    }
}
