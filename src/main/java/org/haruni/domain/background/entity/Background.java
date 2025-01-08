package org.haruni.domain.background.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.haruni.entity.Haruni;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "backgrounds")
public class Background {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "haruni_id", nullable = false, unique = true)
    private Haruni haruni;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "active_level")
    private Double activeLevel = 1.0;

    private Boolean selected = false;
}