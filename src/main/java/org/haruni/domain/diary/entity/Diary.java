package org.haruni.domain.diary.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.haruni.domain.diary.dto.res.DayDiaryResponseDto;
import org.haruni.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diaries")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "s3_img_url")
    private String s3ImgUrl;

    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(nullable = false)
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Diary(DayDiaryResponseDto response, User user, String date){
        this.description = response.getDescription();
        this.s3ImgUrl = response.getDaySummaryImgUrl();
        this.mood = Mood.fromEmotion(response.getMood());
        this.date = date;
        this.user = user;
    }
}