package org.haruni.domain.item.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.haruni.domain.common.dto.res.ResponseDto;
import org.haruni.domain.item.controller.docs.ItemControllerSpecification;
import org.haruni.domain.item.dto.req.ItemSaveRequestDto;
import org.haruni.domain.item.dto.res.SelectedItemResponseDto;
import org.haruni.domain.item.service.ItemService;
import org.haruni.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/item")
public class ItemController implements ItemControllerSpecification {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<SelectedItemResponseDto>>> getSelectedItem(@AuthenticationPrincipal User user){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(itemService.getSelectedItem(user), "선택된 아이탬 조회 완료"));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Boolean>> saveItems(@AuthenticationPrincipal User user,
                                                          @Valid@RequestBody ItemSaveRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(itemService.saveItems(user, request), "아이탬 저장 완료"));
    }

}