package org.haruni.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.haruni.entity.Haruni;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(name = "alarm_active")
    private Boolean alarmActive = false;

    @Column(name = "alarm_active_time")
    private LocalTime alarmActiveTime;

    @Column(name = "haruni_name", length = 50)
    private String haruniName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "role" ,nullable = false)
    private String role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Haruni haruni;

    @OneToMany(mappedBy = "user")
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Chatroom> chatrooms = new ArrayList<>();
}
