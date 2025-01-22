package org.haruni.domain.chatroom.repository;

import org.haruni.domain.chatroom.entity.Chatroom;
import org.haruni.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByUserAndCreatedAt(User user, String createdAt);
}
