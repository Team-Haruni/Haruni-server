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

        return itemRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public Boolean saveItems(UserDetailsImpl authUser, ItemSaveRequestDto request){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        itemRepository.deleteAllByUserId(user.getId());

        request.getItems().forEach(item -> {
            Item newItem = Item.builder()
                    .userId(user.getId())
                    .itemIndex(item.getItemIndex())
                    .build();
            itemRepository.save(newItem);
        });

        log.info("saveItems() - 아이탬 업데이트 성공");

        return true;
    }
}
