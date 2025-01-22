package org.haruni.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.chatroom.entity.Chatroom;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderName;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @Builder
    public Chat(String senderName, Chatroom chatroom, ChatType type, String content, String createdAt){
        this.senderName = senderName;
        this.chatroom = chatroom;
        this.type = type;
        this.content = content;
        this.createdAt = createdAt;
    }
}
