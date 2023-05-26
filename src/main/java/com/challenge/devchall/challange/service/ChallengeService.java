package com.challenge.devchall.challange.service;

import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Transactional
    public void createChallenge(String title, String contents, String status, String frequency, String startDate, String endDate) {

        FormattingResult formattingResult = formatting(status, frequency, startDate, endDate);

        Challenge challenge = Challenge
                .builder()
                .challengeName(title)
                .challengeContents(contents)
                .challengeStatus(formattingResult.formattingStatus)
//                .challengeImg(null)
//                .challengeTag(null)
                .challengeFrequency(formattingResult.formattingFrequency)
                .startDate(formattingResult.formattingStartDate)
                .endDate(formattingResult.formattingEndDate)
                .build();

        challengeRepository.save(challenge);
    }

    public class FormattingResult {
        private boolean formattingStatus;
        private int formattingFrequency;
        private LocalDate formattingStartDate;
        private LocalDate formattingEndDate;

        public FormattingResult(boolean formattingStatus, int formattingFrequency, LocalDate formattingStartDate, LocalDate formattingEndDate) {
            this.formattingStatus = formattingStatus;
            this.formattingFrequency = formattingFrequency;
            this.formattingStartDate = formattingStartDate;
            this.formattingEndDate = formattingEndDate;
        }
    }


    public FormattingResult formatting(String status, String frequency, String start_date, String end_date){

        boolean formattingStatus = formattingStatus(status);
        int formattingFrequency = formattingFrequency(frequency);
        LocalDate formattingStartDate = formattingStartDate(start_date);
        LocalDate formattingEndDate = formattingStartDate(end_date);

        FormattingResult formattingResult = new FormattingResult(formattingStatus, formattingFrequency, formattingStartDate, formattingEndDate);

        return formattingResult;
    }

    public static boolean formattingStatus(String status){

        boolean challengeStatus;

        if(status.equals("비공개")) {
            challengeStatus = false;
        }
        else{
            challengeStatus = true;
        }

        return challengeStatus;
    }

    public static int formattingFrequency(String frequency){

        int challengeFrequency = 0;

        String[] frequencyData = frequency.split("day");

        challengeFrequency = Integer.parseInt(frequencyData[1]);

        return challengeFrequency;

    }

    public static LocalDate formattingStartDate(String start_date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(start_date, formatter);

        return dateTime;
    }

    public static LocalDate formattingEndDate(String end_date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(end_date, formatter);

        return dateTime;
    }
}
