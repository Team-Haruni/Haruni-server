package org.haruni.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.chat.entity.ChatType;
import org.haruni.domain.chat.repository.ChatRepository;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.repository.ChatroomRepository;
import org.haruni.domain.haruni.dto.req.ChatRequestDto;
import org.haruni.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;

    @Transactional
    public void saveUserChat(User user, ChatRequestDto request){

        String formatDate = getNow();

        Chatroom chatroom = chatroomRepository.findByUserAndCreatedAt(user, formatDate)
                .orElseGet(() -> createChatroom(user, formatDate));

        Chat chat = Chat.builder()
                .chatroom(chatroom)
                .type(ChatType.USER)
                .content(request.getContent())
                .createdAt(getCurrentTime())
                .build();

        chatRepository.save(chat);
        chatroom.getChats().add(chat);
    }

    @Transactional
    public Chat saveHaruniChat(User user, String haruniResponse){
        String formatDate = getNow();

        Chatroom chatroom = chatroomRepository.findByUserAndCreatedAt(user, formatDate)
                .orElseGet(() -> createChatroom(user, formatDate));

        Chat chat = Chat.builder()
                .chatroom(chatroom)
                .type(ChatType.HARUNI)
                .content(haruniResponse)
                .createdAt(getCurrentTime())
                .build();

        chatRepository.save(chat);
        chatroom.getChats().add(chat);

        return chat;
    }

    private String getNow() {
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String getCurrentTime() {
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private Chatroom createChatroom(User user, String createdAt){
        Chatroom chatroom = Chatroom.builder()
                .user(user)
                .createdAt(createdAt)
                .build();

        return chatroomRepository.save(chatroom);
    }


}
