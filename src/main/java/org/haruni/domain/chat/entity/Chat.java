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

    @Column(nullable = false)
    private String senderName;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Builder
    public Chat(String senderName, ChatType type, String content, String createdAt) {
        this.senderName = senderName;
        this.type = type;
        this.content = content;
        this.createdAt = createdAt;
    }
}