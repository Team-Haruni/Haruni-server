package org.haruni.domain.chatroom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.chatroom.repository.ChatroomRepository;
import org.haruni.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;

    public Chatroom createChatroom(User user, String createdAt){
        Chatroom chatroom = Chatroom.builder()
                .userId(user.getId())
                .createdAt(createdAt)
                .build();

        user.getChatrooms().add(chatroom);

        log.info("createChatroom() : 채팅룸 생성 완료");

        return chatroomRepository.save(chatroom);
    }
}
