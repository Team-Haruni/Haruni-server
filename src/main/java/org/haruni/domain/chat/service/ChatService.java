package org.haruni.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.model.dto.res.HaruniChatResponseDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.entity.ChatType;
import org.haruni.domain.chat.repository.ChatRepository;
import org.haruni.domain.common.util.TimeUtils;
import org.haruni.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public void saveUserChat(User user, ChatRequestDto request){

        Chat chat = Chat.builder()
                .chatType(ChatType.USER)
                .userId(user.getId())
                .content(request.getContent())
                .sendingDate(TimeUtils.getCurrentDate())
                .sendingTime(TimeUtils.getCurrentTime())
                .build();

        chatRepository.save(chat);

        log.info("saveUserChat() - 유저 채팅 저장 성공");
    }

    public Chat saveHaruniChat(User user, HaruniChatResponseDto response){
        Chat chat = Chat.builder()
                .chatType(ChatType.HARUNI)
                .userId(user.getId())
                .content(response.getResponse())
                .sendingDate(TimeUtils.getCurrentDate())
                .sendingTime(TimeUtils.getCurrentTime())
                .build();

        log.info("saveHaruniChat() - 하루니 채팅 저장 성공");

        return chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDto> getChats(Long userId, String date){
        log.info("getChats() - {}의 채팅 조회 완료", date);
        return chatRepository.findByUserIdAndDate(userId, date);
    }
}