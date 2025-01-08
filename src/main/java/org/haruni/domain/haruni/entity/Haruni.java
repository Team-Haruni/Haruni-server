package org.haruni.domain.haruni.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.background.entity.Background;
import org.haruni.domain.item.entity.Item;
import org.haruni.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(nullable = false)
    private Double level = 1.0;

    @OneToMany(mappedBy = "haruni", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToOne(mappedBy = "haruni", cascade = CascadeType.ALL)
    private Background background;
}
