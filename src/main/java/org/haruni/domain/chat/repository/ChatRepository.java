package org.haruni.domain.chat.repository;

import org.haruni.domain.chat.dto.req.ChatDto;
import org.haruni.domain.chat.dto.res.ChatResponseDto;
import org.haruni.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
        SELECT c.userId
        FROM Chat c
        WHERE c.sendingDate = :date
        GROUP BY c.userId
        HAVING COUNT(c) >= :chatCount
    """)
    List<Long> findUserIdsByMinChatsOnDate(@Param("chatCount") long chatCount, @Param("date") String date);

    @Query("""
        SELECT new org.haruni.domain.chat.dto.req.ChatDto(c.userId, c.content, c.sendingDate, c.sendingTime)
        FROM Chat c
        WHERE c.chatType = :chatType
        AND c.sendingDate = :sendingDate
        AND c.userId = :userId
    """)
    List<ChatDto> findAllByUserIdAndSendingDate(@Param("userId") Long userId, @Param("sendingDate") String sendingDate, @Param("chatType") String chatType);

    @Query("""
        SELECT new org.haruni.domain.chat.dto.res.ChatResponseDto(c.chatType, c.content, c.sendingTime)
        FROM Chat c
        WHERE c.userId = :userId
        AND c.sendingDate = :date
        ORDER BY c.id ASC
    """)
    List<ChatResponseDto> findByUserIdAndDate(@Param("userId")Long userId, @Param("date") String date);
}
