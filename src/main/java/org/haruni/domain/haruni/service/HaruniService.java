package org.haruni.domain.haruni.service;

import lombok.extern.log4j.Log4j2;
import org.haruni.domain.chat.dto.req.ChatRequestBody;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.chat.dto.res.ChatResponseBody;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.chat.service.ChatService;
import org.haruni.domain.haruni.dto.req.HaruniExpIncrementRequestDto;
import org.haruni.domain.haruni.dto.res.MainPageResponseDto;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.haruni.repository.HaruniRepository;
import org.haruni.domain.item.dto.res.SelectedItemResponseDto;
import org.haruni.domain.item.repository.ItemRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
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
import java.util.List;

@Log4j2
@Service
public class HaruniService {

    private final ChatService chatService;

    private final HaruniRepository haruniRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final RestTemplate modelServerTemplate;

    public HaruniService(ChatService chatService, HaruniRepository haruniRepository, UserRepository userRepository, @Qualifier("modelServerTemplate") RestTemplate modelServerTemplate, ItemRepository itemRepository) {
        this.chatService = chatService;
        this.haruniRepository = haruniRepository;
        this.userRepository = userRepository;
        this.modelServerTemplate = modelServerTemplate;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public MainPageResponseDto getHaruni(UserDetailsImpl authUser){

        Haruni haruni = haruniRepository.findByUserId(authUser.getUser().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        String greetingMessage = getGreetingMessage(now, authUser.getUser().getNickname());

        List<SelectedItemResponseDto> itemIndexes = itemRepository.findAllByUserId(authUser.getUser().getId());

        log.info("getHaruni() - 하루니 조회 성공");

        return MainPageResponseDto.builder()
                .haruniLevelInteger(haruni.getHaruniLevelInteger())
                .haruniLevelDecimal(haruni.getHaruniLevelDecimal())
                .greetingMessage(greetingMessage)
                .itemIndexes(itemIndexes)
                .build();
    }

    @Transactional
    public ChatResponseDto sendChatToHaruni(UserDetailsImpl authUser, ChatRequestDto request){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        chatService.saveUserChat(user, request);

        Haruni haruni = haruniRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        haruni.incrementExp(3.3);

        log.info("sendChatToHaruni() - 사용자 채팅 저장 성공");

        ChatRequestBody requestBody = ChatRequestBody.builder()
                .user(user)
                .haruniPersonality(haruni.getPersonality())
                .content(request.getContent())
                .build();

        ChatResponseBody responseBody;
        try{
            responseBody = modelServerTemplate.postForObject(
                    "/api/v1/question",
                    requestBody,
                    ChatResponseBody.class
            );
            log.info("sendChatToHaruni() - 하루니 채팅 전송 성공");

            return ChatResponseDto.entityToDto(
                    chatService.saveHaruniChat(user, responseBody)
            );
        }catch (HttpClientErrorException e){
            log.error("sendChatToHaruni() - 하루니 채팅 전송 실패 [{}] - {}", e.getStatusText(), e.getMessage());
            throw new RestApiException(CustomErrorCode.POST_MESSAGE_TO_MODEL_SERVER_FAILED);
        }
    }

    public List<ChatResponseDto> getChats(UserDetailsImpl authUser, String request){
        return chatService.getChats(authUser.getUser().getId(), request);
    }

    @Transactional
    public Double incrementHaruniExp(UserDetailsImpl authUser, HaruniExpIncrementRequestDto request){
        Haruni haruni = haruniRepository.findByUserId(authUser.getUser().getId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.HARUNI_NOT_FOUND));

        haruni.incrementExp(request.getExp());

        log.info("incrementHaruniExp() - 하루니 레벨 조정 완료 {} -> {}", haruni.getLevel() - request.getExp(), haruni.getLevel());

        return haruni.getLevel();
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
