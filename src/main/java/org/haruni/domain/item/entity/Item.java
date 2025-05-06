package org.haruni.domain.item.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(hidden = true)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "item_type")
    private String itemType;

    @Column(nullable = false, length = 100, name = "item_index")
    private Long itemIndex;

    @Builder
    private Item(Long userId, String itemType, Long itemIndex) {
        this.itemType = itemType;
        this.userId = userId;
        this.itemIndex = itemIndex;
    }
}