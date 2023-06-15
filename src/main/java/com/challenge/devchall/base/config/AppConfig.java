package com.challenge.devchall.base.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Getter
    private static int titleLength;

    @Value("${custom.challenge.titleLength}")
    public void setTitleLength(int titleLength) {
        AppConfig.titleLength = titleLength;
    }

    @Getter
    private static int contentLength;

    @Value("${custom.challenge.contentLength}")
    public void setContentLength(int contentLength){
        AppConfig.contentLength = contentLength;
    }

    @Getter
    private static int memberLimit;

    @Value("${custom.challenge.memberLimit}")
    public void setMemberLimit(int memberLimit){
        AppConfig.memberLimit = memberLimit;
    }

    @Getter
    private static int weeklyPoint;

    @Value("${custom.challenge.weeklyPoint}")
    public void setWeeklyPoint(int weeklyPoint){
        AppConfig.weeklyPoint = weeklyPoint;
    }

    @Getter
    private static int pageable;

    @Value("${custom.challenge.pageable}")
    public void setPageable(int pageable){
        AppConfig.pageable = pageable;
    }

    @Getter
    private static int reportCount;

    @Value("${custom.challenge.reportCount}")
    public void setReportCount(int reportCount){
        AppConfig.reportCount = reportCount;
    }

}
