package org.haruni.domain.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static String getCurrentDate(){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getCurrentTime(){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return now.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String getDateDaysAgo(long day){
        LocalDateTime past = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(8);
        return past.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
