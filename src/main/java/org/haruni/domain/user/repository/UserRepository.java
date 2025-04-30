package org.haruni.domain.user.repository;

import org.haruni.domain.user.dto.res.UserAlarmDto;
import org.haruni.domain.user.dto.res.UserSummaryDto;
import org.haruni.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
        SELECT new org.haruni.domain.user.dto.res.UserAlarmDto(u.fcmToken, u.alarmActiveTime)
        FROM User u
        WHERE u.alarmActive = true
    """)
    List<UserAlarmDto> findAlarmByAlarmActive();
    
    User findByFcmToken(String fcmToken);

    boolean existsByFcmToken(String fcmToken);

    @Query("SELECT new org.haruni.domain.user.dto.res.UserSummaryDto(u.id, u.gender, u.mbti) " +
            "FROM User u " +
            "WHERE u.id IN :userIds")
    List<UserSummaryDto> findUserSummariesByUserIds(@Param("userIds") List<Long> userIds);
}
