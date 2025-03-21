package org.haruni.domain.user.repository;

import org.haruni.domain.alarm.entity.Alarm;
import org.haruni.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new org.haruni.domain.alarm.entity.Alarm(u.fcmToken,u.alarmActiveTime) " +
            "FROM User u " +
            "WHERE u.alarmActive = true")
    List<Alarm> findAlarmByAlarmActive();

    User findByFcmToken(String fcmToken);

    boolean existsByFcmToken(String fcmToken);
}
