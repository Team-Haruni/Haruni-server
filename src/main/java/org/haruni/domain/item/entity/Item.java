package org.haruni.domain.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.item.dto.req.ItemRequestDto;
import org.haruni.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String itemImgUrl;

    @Column(name = "active_level", nullable = false)
    private Double activeLevel;

    @Column(name = "x_position")
    private Double xPosition;

    @Column(name = "y_position")
    private Double yPosition;

    @Builder
    protected Item(User user, ItemRequestDto request){
        this.user = user;
        this.itemImgUrl = request.getItemImgUrl();
        this.activeLevel = request.getActiveLevel();
        this.xPosition = request.getXPosition();
        this.yPosition = request.getYPosition();
    }
}
