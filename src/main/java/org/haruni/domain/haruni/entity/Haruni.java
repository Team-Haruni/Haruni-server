package org.haruni.domain.haruni.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Double level;

    @Builder
    private Haruni(Long userId, String name) {
        this.userId = userId;
        this.name = name;
        this.level = 0.0;
    }

    public void incrementExp(Double exp){
        this.level += exp;
    }
}
