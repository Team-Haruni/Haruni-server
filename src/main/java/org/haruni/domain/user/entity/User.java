package org.haruni.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.haruni.entity.Haruni;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
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
    private Boolean alarmActive;

    @Column(name = "alarm_active_time")
    private LocalTime alarmActiveTime;

    @Column(name = "haruni_name", length = 50)
    private String haruniName;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
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

    @Builder
    protected User(SignUpRequestDto req, String encodedPassword, Haruni haruni){
        this.email = req.getEmail();
        this.password = encodedPassword;
        this.nickname = req.getNickname();
        this.alarmActive = req.getAlarmActive();
        this.alarmActiveTime = req.getAlarmActiveTime();
        this.haruniName = req.getHaruniName();
        this.role = "ROLE_USER";
        this.haruni = haruni;
    }
}