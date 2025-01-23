package org.haruni.domain.chatroom.repository;

import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByUserAndCreatedAt(User user, String createdAt);

    @Query("SELECT c FROM Chatroom c WHERE c.createdAt = :date")
    List<Chatroom> findByCreatedAt(@Param("date") String date);
}
