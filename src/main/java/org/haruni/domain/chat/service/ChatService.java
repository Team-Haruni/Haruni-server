package org.haruni.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.chat.dto.req.ChatRequestDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.entity.ChatType;
import org.haruni.domain.chat.repository.ChatRepository;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.repository.ChatroomRepository;
import org.haruni.domain.chatroom.service.ChatroomService;
import org.haruni.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatroomService chatroomService;

    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;

    public void saveUserChat(User user, ChatRequestDto request){

        String formatDate = getNow();

        Chatroom chatroom = chatroomRepository.findByUserAndCreatedAt(user, formatDate)
                .orElseGet(() -> chatroomService.createChatroom(user, formatDate));

        Chat chat = Chat.builder()
                .senderName(user.getNickname())
                .chatroom(chatroom)
                .type(ChatType.USER)
                .content(request.getContent())
                .createdAt(getCurrentTime())
                .build();

        chatRepository.save(chat);
        chatroom.getChats().add(chat);
    }

    public Chat saveHaruniChat(User user, String haruniName, String haruniResponse){
        String formatDate = getNow();

        Chatroom chatroom = chatroomRepository.findByUserAndCreatedAt(user, formatDate)
                .orElseGet(() -> chatroomService.createChatroom(user, formatDate));

        Chat chat = Chat.builder()
                .senderName(haruniName)
                .chatroom(chatroom)
                .type(ChatType.HARUNI)
                .content(haruniResponse)
                .createdAt(getCurrentTime())
                .build();

        chatRepository.save(chat);
        chatroom.getChats().add(chat);

        return chat;
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDto> getChats(User user, String request){
        log.info("[ChatService - getChats()] - 채팅 조회 시작");

        /**
         * Chat 테이블에서 유저 아이디를 통회 조회하고 id를 기준으로 내림차순 조회
         *
         */

        Chatroom chatroom = chatroomRepository.findByUserAndCreatedAt(user, request)
                .orElse(null);

        if(chatroom == null){
            log.warn("[ChatService - getChats()] - 채팅 내역이 존재하지 않습니다");
            return Collections.emptyList();
        }

        log.info("[ChatService - getChats()] - 채팅 조회 종료");

        return chatroom.getChats().stream()
                .map(ChatResponseDto::entityToDto)
                .toList();
    }

    public String getNow() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getCurrentTime() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}