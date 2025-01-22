package org.haruni.domain.chatroom.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.chat.entity.Chat;
import org.haruni.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatrooms")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    @Builder
    public Chatroom(User user, String createdAt){
        this.user = user;
        this.createdAt = createdAt;
    }
}
