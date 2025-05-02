package org.haruni.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.haruni.domain.haruni.entity.MBTI;
import org.haruni.domain.oauth.common.utils.OAuth2Provider;
import org.haruni.domain.user.dto.req.SignUpRequestDto;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Schema(hidden = true)
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_id")
    private OAuth2Provider providerId;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = true, length = 100)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MBTI mbti;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    @Column(name = "alarm_active")
    private Boolean alarmActive;

    @Column(name = "alarm_active_time")
    private String alarmActiveTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private User(SignUpRequestDto request, String encodedPassword) {
        this.providerId = OAuth2Provider.fromOAuth2Provider(request.getProviderId());
        this.email = request.getEmail();
        this.password = encodedPassword;
        this.role = "ROLE_USER";
        this.nickname = request.getNickname();
        this.gender = Gender.fromGender(request.getGender());
        this.mbti = MBTI.fromMBTI(request.getMbti());
        this.fcmToken = request.getFcmToken();
        this.alarmActive = request.getAlarmActive();
        this.alarmActiveTime = request.getAlarmActiveTime();
    }

    public void updateAlarmActiveTime(String alarmActiveTime) {
        this.alarmActiveTime = alarmActiveTime;
    }
}