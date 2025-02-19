package org.haruni.domain.diary.repository;

import org.haruni.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByUserIdAndDate(Long userId, String date);

    @Query(value = "SELECT * FROM diaries WHERE user_id = :userId AND date LIKE CONCAT(:yearMonth, '%')", nativeQuery = true)
    List<Diary> findAllByUserAndStartWithMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);
}
