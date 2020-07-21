package com.project.backend.Configurations;
import java.text.*;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.stereotype.Component;


public class GetTimeZone {

    
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss (z Z)");

    public static String getSeoulDate() {
        TimeZone time;
        Date date = new Date();
        time = TimeZone.getTimeZone("Asia/Seoul");
        
        df.setTimeZone(time);
        return df.format(date);

    }

}