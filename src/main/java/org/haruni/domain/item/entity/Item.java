package org.haruni.domain.item.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.item.dto.req.ItemRequestDto;

@Schema(hidden = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String objectKey;

    @Column(name = "active_level", nullable = false)
    private Double activeLevel;

    @Column(name = "x_position")
    private Double xPosition;

    @Column(name = "y_position")
    private Double yPosition;

    @Builder
    protected Item(ItemRequestDto request) {
        this.objectKey = request.getItemImgUrl();
        this.activeLevel = request.getActiveLevel();
        this.xPosition = request.getXPosition();
        this.yPosition = request.getYPosition();
    }
}