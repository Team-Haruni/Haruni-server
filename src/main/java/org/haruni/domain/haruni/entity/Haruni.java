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

    @Column(name = "haruni_image_url", nullable = false)
    private String haruniImageUrl = "https://haruni-ai.site/image";

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(nullable = false)
    private Double level = 1.0;

    @Builder
    private Haruni(String name, String prompt){
        this.name = name;
        this.prompt = prompt;
    }

    public void matchUser(User user) {
        this.user = user;
    }
    public void updatePrompt(String prompt){ this.prompt = prompt; }
}
