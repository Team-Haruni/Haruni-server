package org.haruni.domain.haruni.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.haruni.domain.user.entity.User;

@Schema(hidden = true)
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "haruni")
public class Haruni {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String haruniImgUrl = "https://";

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(nullable = false)
    private Double level = 1.0;

    @Builder
    private Haruni(String name, MBTI mbti, String prompt){
        this.name = name;
        this.mbti = mbti;
        this.prompt = prompt;
    }

    public void matchUser(User user) {
        this.user = user;
    }
    public void updatePrompt(String prompt){ this.prompt = prompt; }
}
