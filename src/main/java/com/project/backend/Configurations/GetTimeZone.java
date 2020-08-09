package com.project.backend.Configurations;
import java.text.*;
import java.util.Date;
import java.util.TimeZone;


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
    private static TimeZone seoulTime = TimeZone.getTimeZone("Asia/Seoul");
    public static String getSeoulDate() {


        Date date = new Date();

        df.setTimeZone(seoulTime);
        return df.format(date);
    }

    public static String getTimeToDate(long t) {

        Date date = new Date();
        date.setTime(t);
       
        
        df.setTimeZone(seoulTime);
        return df.format(date);
    }

    public static Long DateToGetTime(Date date) {

        return date.getTime();
    }


}