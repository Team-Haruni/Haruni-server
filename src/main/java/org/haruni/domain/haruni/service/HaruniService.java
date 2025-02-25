package org.haruni.domain.haruni.service;

import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.chat.dto.req.ChatRequestBody;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.chat.service.ChatService;
import org.haruni.domain.haruni.dto.req.HaruniInstanceCreateRequestDto;
import org.haruni.domain.haruni.dto.req.PromptUpdateRequestDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class HaruniService {

    private final ChatService chatService;
    private final HaruniRepository haruniRepository;
    private final UserRepository userRepository;
    private final RestTemplate modelServerTemplate;

    public HaruniService(ChatService chatService, HaruniRepository haruniRepository, UserRepository userRepository, @Qualifier("modelServerTemplate") RestTemplate modelServerTemplate) {
        this.chatService = chatService;
        this.haruniRepository = haruniRepository;
        this.userRepository = userRepository;
        this.modelServerTemplate = modelServerTemplate;
    }

    @Async
    public void createHaruniInstance(Long haruniId) {
        try {
            Haruni haruni = haruniRepository.findById(haruniId)
                    .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

            HaruniInstanceCreateRequestDto requestBody = HaruniInstanceCreateRequestDto
                    .builder()
                    .userId(haruni.getUser().getId())
                    .haruniId(haruniId)
                    .prompt(haruni.getPrompt())
                    .build();

            modelServerTemplate.postForObject("/chat", requestBody, String.class);

            // TODO 인스턴스 생성 완료시, 하루니 고유키 반환 로직 추가 후 로그에 출력

            log.info("[HaruniService - createHaruniInstance()] - 하루니 인스턴스 생성 성공");

            CompletableFuture.completedFuture(true);

        } catch (HttpClientErrorException e) {
            log.error("[HaruniService - createHaruniInstance()] - 하루니 인스턴스 생성 실패 [{}] - {}",
                    e.getStatusText(), e.getMessage());
            CompletableFuture.completedFuture(false);

        } catch (Exception e) {
            log.error("[HaruniService - createHaruniInstance()] - 하루니 인스턴스 생성 실패 {}",
                    e.getMessage());
            CompletableFuture.completedFuture(false);
        }
    }

    @Transactional(readOnly = true)
    public MainPageResponseDto getHaruni(User user){

        Haruni haruni = haruniRepository.findById(user.getHaruni().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        String greetingMessage = getGreetingMessage(now, user.getNickname());

        log.info("[HaruniService - getHaruni()] - 하루니 조회 성공");

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

        Haruni haruni = haruniRepository.findById(user.getHaruni().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        haruni.updatePrompt(request.getPrompt());

        log.info("[HaruniService - updatePrompt()] - 하루니 프롬프트 수정 성공");

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
    public ChatResponseDto sendChatToHaruni(User authUser, ChatRequestDto request){

        User user = userRepository.findByEmail(authUser.getEmail())
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        chatService.saveUserChat(user, request);

        log.info("[HaruniService - sendChatToHaruni()] - 사용자 채팅 저장 성공");

        Haruni haruni = haruniRepository.findById(authUser.getHaruni().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        ChatRequestBody requestBody = ChatRequestBody.builder()
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
            log.info("[HaruniService - sendChatToHaruni()] - 하루니 채팅 전송 성공");

            return ChatResponseDto.entityToDto(
                    chatService.saveHaruniChat(user, haruni.getName(), responseBody)
            );
        }catch (HttpClientErrorException e){
            log.error("[HaruniService - sendChatToHaruni()] - 하루니 채팅 전송 실패 [{}] - {}", e.getStatusText(), e.getMessage());
            throw new RestApiException(CustomErrorCode.POST_MESSAGE_TO_MODEL_SERVER_FAILED);
        }
    }

    public List<ChatResponseDto> getChats(User user, String request){
        return chatService.getChats(user, request);
    }
}
