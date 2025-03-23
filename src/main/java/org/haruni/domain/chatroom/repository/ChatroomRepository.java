package org.haruni.domain.chatroom.repository;

import org.haruni.domain.chatroom.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByUserIdAndCreatedAt(Long userId, String createdAt);

    @Query("SELECT c FROM Chatroom c WHERE c.createdAt = :date")
    List<Chatroom> findByCreatedAt(@Param("date") String date);

    @Query("""
        SELECT c FROM Chatroom c
        WHERE SIZE(c.chats) >= :size
        AND c.createdAt = :date
    """)
    List<Chatroom> findByChatSizeAndCreatedAt(@Param("size") Long size, @Param("date") String date);

    @Query("SELECT cr.userId FROM Chatroom cr JOIN cr.chats c " +
            "WHERE cr.createdAt = :date " +
            "GROUP BY cr.userId " +
            "HAVING COUNT(c) >= :minChats")
    List<Long> findUserIdsByChatCountAndCreatedAt(@Param("minChats") Long minChats,
                                                  @Param("date") String date);

    @Query("SELECT cr FROM Chatroom cr JOIN FETCH cr.chats " +
            "WHERE cr.userId IN :userIds AND cr.createdAt = :date")
    List<Chatroom> findChatroomByUserIdsAndCreatedAt(@Param("userIds") List<Long> userIds,
                                                      @Param("date") String date);
}
