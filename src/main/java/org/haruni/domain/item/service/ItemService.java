package org.haruni.domain.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SelectedItemResponseDto> getSelectedItem(UserDetailsImpl authUser){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        log.info("getSelectedItem() - 사용자 아이탬 조회 완료");

        return itemRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public Boolean saveItems(UserDetailsImpl authUser, ItemSaveRequestDto request){

        User user = userRepository.findByEmail(authUser.getUser().getEmail())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.USER_NOT_FOUND));

        itemRepository.deleteAllByUserId(user.getId());

        if(request.getItems().isEmpty())
            throw new RestApiException(CustomErrorCode.TARGET_ITEMS_NOT_FOUND);

        request.getItems().forEach(item -> {
            Item newItem = Item.builder()
                    .userId(user.getId())
                    .itemType(item.getItemType())
                    .itemIndex(item.getItemIndex())
                    .build();
            itemRepository.save(newItem);
        });

        log.info("saveItems() - 사용자 아이탬 업데이트 완료");

        return true;
    }
}
