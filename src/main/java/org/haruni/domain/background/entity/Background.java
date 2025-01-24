package org.haruni.domain.background.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.user.entity.User;

@Table(name = "backgrounds")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Background {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String backgroundImgUrl;

    @Column(name = "active_level")
    private Double activeLevel = 1.0;
}