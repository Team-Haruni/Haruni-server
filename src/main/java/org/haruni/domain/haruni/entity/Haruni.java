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
    private String personality;

    @Column(nullable = false)
    private Double level;

    @Builder
    private Haruni(Long userId, String name, String personality) {
        this.userId = userId;
        this.name = name;
        this.personality = personality;
        this.level = 0.0;
    }

    @Transient
    public Double getHaruniLevelInteger() {
        if (level <= 100.0) {
            return 1.0;
        } else if (level <= 500.0) {
            return 2.0;
        } else if (level <= 1000.0) {
            return 3.0;
        } else {
            return Math.floor((level - 1) / 500.0) + 1;
        }
    }

    @Transient
    public Double getHaruniLevelDecimal() {
        double[] thresholds = {0.0, 100.0, 500.0, 1000.0};
        int lvl = getHaruniLevelInteger().intValue();
        int idx = Math.min(lvl, thresholds.length - 1);

        double min = thresholds[idx - 1];
        double max = thresholds[idx];
        double range = max - min;

        if (range <= 0)
            return 100.0;

        double progress = Math.max(0.0, Math.min(level - min, range));

        return (progress / range) * 100.0;
    }

    public void incrementExp(Double exp){
        this.level += exp;
    }
}
