package org.haruni.domain.item.repository;

import org.haruni.domain.item.dto.res.SelectedItemResponseDto;
import org.haruni.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
        SELECT new org.haruni.domain.item.dto.res.SelectedItemResponseDto(i.itemType, i.itemIndex)
        FROM Item i
        WHERE i.userId = :userId
    """)
    List<SelectedItemResponseDto> findAllByUserId(@Param("userId") Long userId);

    void deleteAllByUserId(Long userId);
}
