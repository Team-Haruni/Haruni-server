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

        log.info("[ChatService - saveUserChat()] - 유저 채팅 저장 성공");
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

        log.info("[ChatService - saveHaruniChat()] - 하루니 채팅 저장 성공 {} - {}", user.getEmail(), haruniName);

        return chat;
    }

    @Transactional(readOnly = true)
    public List<ChatResponseDto> getChats(User user, String request){

        Chatroom chatroom = chatroomRepository.findByUserAndCreatedAt(user, request)
                .orElse(null);

        if(chatroom == null){
            log.warn("[ChatService - getChats()] - 채팅 내역이 존재하지 않습니다");
            return Collections.emptyList();
        }

        log.info("[ChatService - getChats()] - 채팅 조회 성공");

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