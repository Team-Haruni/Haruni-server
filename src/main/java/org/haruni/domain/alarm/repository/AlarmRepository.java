package org.haruni.domain.alarm.repository;

import org.haruni.domain.alarm.entity.Alarm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends CrudRepository<Alarm, String> {

    void deleteAllBySendingTime(String sendingTime);

    List<Alarm> findAllBySendingTime(String sendingTime);

    void deleteByFcmToken(String fcmToken);
}
