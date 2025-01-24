package org.haruni.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.item.dto.req.ItemSaveRequestDto;
import org.haruni.domain.item.dto.res.SelectedItemResponseDto;
import org.haruni.domain.item.entity.Item;
import org.haruni.domain.item.repository.ItemRepository;
import org.haruni.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<SelectedItemResponseDto> getSelectedItem(User user){
        log.info("[ItemService - getSelectedItem()] - In");

        log.info("[ItemService - getSelectedItem()] - Out");
        return user.getItems().stream()
                .map(SelectedItemResponseDto::entityToDto)
                .toList();
    }

    @Transactional
    public Boolean saveItems(User user, ItemSaveRequestDto request){
        log.info("[ItemService - saveItems()] - In");

        user.getItems().clear();
        itemRepository.deleteAllByUser(user);

        log.info("[ItemService - saveItems()] - User's Item Clear Succeed");

        List<Item> newItems = request.getItems().stream()
                .map(item -> Item.builder()
                        .user(user)
                        .request(item)
                        .build())
                .toList();

        user.getItems().addAll(newItems);
        itemRepository.saveAll(newItems);

        log.info("[ItemService - saveItems()] - User's Item Save Succeed");
        log.info("[ItemService - saveItems()] - Out");

        return true;
    }
}
