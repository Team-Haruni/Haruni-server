package org.haruni.domain.alarm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmContent {
    HAPPY_MOMENT("오늘 하루를 기쁘게 했던 순간이 있었나요?"),
    SMALL_SUCCESS("작은 성공이라도 스스로를 칭찬하고 싶은 일이 있나요?"),
    GRATEFUL_THING("오늘 감사했던 순간을 떠올려볼까요?"),
    NEW_DISCOVERY("오늘은 어떤 새로운 발견이 있었나요?"),
    FAVORITE_TIME("오늘 하루 중 가장 좋았던 시간은 언제인가요?"),
    FOOD_MEMORY("오늘 먹은 음식 중 가장 맛있었던 게 뭔가요?"),
    TOMORROW_WISH("내일은 어떤 일이 있었으면 좋겠어요?"),
    TODAY_WEATHER("오늘 날씨는 마음에 들었나요?"),
    UNEXPECTED_JOY("예상치 못한 즐거움이 있었다면 무엇인가요?"),
    HEALING_MOMENT("오늘 나를 위로해준 순간이 있었나요?"),
    ENERGY_SOURCE("오늘 나에게 힘이 되어준 것은 무엇인가요?"),
    LITTLE_HOBBY("오늘은 취미 생활을 즐길 수 있었나요?"),
    PEOPLE_AROUND("오늘 만난 사람 중 기억에 남는 사람이 있나요?"),
    SELF_CARE("오늘 나를 위해 특별히 해준 것이 있나요?"),
    MUSIC_TODAY("오늘 들은 노래 중 기분 좋았던 곡이 있나요?"),
    LEARN_TODAY("오늘은 새롭게 알게 된 것이 있나요?"),
    COMFORT_ZONE("오늘 가장 편안했던 순간은 언제였나요?"),
    TOMORROW_PLAN("내일은 어떤 하루를 보내고 싶으신가요?"),
    HAPPY_NEWS("오늘 기분 좋은 소식을 들으셨나요?"),
    NATURE_MOMENT("오늘 하늘을 올려다본 순간이 있었나요?");

    private final String message;

    public static String getRandomMessage() {
        AlarmContent[] contents = values();
        return contents[(int) (System.currentTimeMillis() % contents.length)].getMessage();
    }
}
