package org.haruni.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "chat_type")
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "content")
    private String content;

    @Column(nullable = false, name = "sending_date")
    private String sendingDate;

    @Column(nullable = false, name = "sending_time")
    private String sendingTime;

    @Builder
    private Chat(ChatType chatType, Long userId, String content, String sendingDate, String sendingTime) {
        this.chatType = chatType;
        this.userId = userId;
        this.content = content;
        this.sendingDate = sendingDate;
        this.sendingTime = sendingTime;
    }
}