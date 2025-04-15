package org.haruni.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.haruni.domain.item.dto.req.ItemSaveRequestDto;
import org.haruni.domain.item.dto.res.SelectedItemResponseDto;
import org.haruni.domain.item.entity.Item;
import org.haruni.domain.item.repository.ItemRepository;
import org.haruni.domain.user.entity.User;
import org.haruni.domain.user.entity.UserDetailsImpl;
import org.haruni.domain.user.repository.UserRepository;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SelectedItemResponseDto> getSelectedItem(UserDetailsImpl authUser){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        log.info("getSelectedItem() - 선택된 아이탬 조회 성공");

        return user.getItems().stream()
                .map(SelectedItemResponseDto::entityToDto)
                .toList();
    }

    @Transactional
    public Boolean saveItems(UserDetailsImpl authUser, ItemSaveRequestDto request){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        user.getItems().clear();

        List<Item> newItems = request.getItems().stream()
                .map(item -> Item.builder()
                        .request(item)
                        .build())
                .toList();

        user.getItems().addAll(newItems);
        itemRepository.saveAll(newItems);

        log.info("saveItems() - 아이탬 저장 성공");

        return true;
    }
}
