package org.haruni.domain.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.haruni.entity.Haruni;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "haruni_id", nullable = false)
    private Haruni haruni;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "active_level")
    private Double activeLevel = 1.0;

    private Boolean selected = false;

    @Column(name = "x_position")
    private Double xPosition;

    @Column(name = "y_position")
    private Double yPosition;
}
