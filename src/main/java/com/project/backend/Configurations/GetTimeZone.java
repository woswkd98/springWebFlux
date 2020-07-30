package com.project.backend.Configurations;
import java.text.*;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.stereotype.Component;


public class GetTimeZone {

    /*
    
         req -> ok().body( databaseClient
                .execute(
                    "insert into testtable(asdf, date, datet)" +
                    "values(12345, :date,:datet)")
                    .bind("date", GetTimeZone.getSeoulDate()) // date 는 시간 짤림
                    .bind("datet", GetTimeZone.getSeoulDate()) // dateTime 시간 안짤림 이렇게 해서 스트링으로 저장해도 date 타입에 저장가능
                    .fetch().rowsUpdated()
                    .onErrorReturn(0), Integer.class));
    */
    
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getSeoulDate() {
        TimeZone time;

        Date date = new Date();
        time = TimeZone.getTimeZone("Asia/Seoul");
        System.out.println(time);
        df.setTimeZone(time);
        return df.format(date);
    }

    


}