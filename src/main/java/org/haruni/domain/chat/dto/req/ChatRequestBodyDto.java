package org.haruni.domain.chat.dto.req;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRequestBodyDto {
    private Long haruniId;
    private String content;
}
