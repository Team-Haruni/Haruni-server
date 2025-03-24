package org.haruni.domain.chatroom.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.chat.entity.Chat;

import java.util.ArrayList;
import java.util.List;

@Schema(hidden = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatrooms")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatroom_id")
    private List<Chat> chats = new ArrayList<>();

    @Builder
    private Chatroom(Long userId, String createdAt) {
        this.userId = userId;
        this.createdAt = createdAt;
    }
}