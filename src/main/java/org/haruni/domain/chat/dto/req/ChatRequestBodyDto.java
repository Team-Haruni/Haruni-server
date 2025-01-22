package org.haruni.domain.chat.dto.req;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRequestBodyDto {

    // CONCEPT : 하루니 모델 서버에 전송할 채팅 양식(Spring -> AI)

    private Long haruniId;
    private String content;
}
