package org.haruni.domain.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.repository.ChatroomRepository;
import org.haruni.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    public Chatroom createChatroom(User user, String createdAt){
        Chatroom chatroom = Chatroom.builder()
                .user(user)
                .createdAt(createdAt)
                .build();

        return chatroomRepository.save(chatroom);
    }
}
