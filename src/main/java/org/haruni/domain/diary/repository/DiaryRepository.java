package org.haruni.domain.diary.repository;

import org.haruni.domain.diary.dto.req.DiaryDto;
import org.haruni.domain.diary.entity.Diary;
import org.haruni.domain.feedback.dto.res.DayMood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(value = "SELECT * FROM diaries WHERE user_id = :userId AND date LIKE CONCAT(:yearMonth, '%')", nativeQuery = true)
    List<Diary> findAllByUserAndStartWithMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);

    @Query("""
        SELECT d
        FROM Diary d
        WHERE d.userId = :userId AND d.date = :date
    """)
    Optional<Diary> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") String date);


    @Query("""
        SELECT d.userId
        FROM Diary d
        WHERE d.date >= :startDate AND d.date <= :endDate
    """)
    List<Long> findUserIdsByDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("""
        SELECT new org.haruni.domain.diary.dto.req.DiaryDto(d.date, d.mood, d.daySummaryDescription)
        FROM Diary d
        WHERE d.userId = :userId AND d.date >= :startDate AND d.date <= :endDate
    """)
    List<DiaryDto> findDiariesByDateBetween(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("""
        SELECT new org.haruni.domain.feedback.dto.res.DayMood(d.date, d.mood)
        FROM Diary d
        WHERE d.userId = :userId AND d.date >= :startDate AND d.date <= :endDate
    """)
    List<DayMood> findDayMoodByDateBetween(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
