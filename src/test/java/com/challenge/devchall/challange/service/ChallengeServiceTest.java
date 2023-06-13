package com.challenge.devchall.challange.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@Transactional
class ChallengeServiceTest {

    @Autowired
    private ChallengeMemberService challengeMemberService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ChallengePostService challengePostService;

    private Member user3;
    private MultipartFile file;
    private String challengeTitle;
    private String challengeContents;
    private boolean challengeStatus;
    private int challengeFrequency;
    private String challengeStartDate;
    private int challengePeriod;
    private String challengeLanguage;
    private String challengeSubject;
    private String challengePostType;

    @BeforeEach
    void setUp() throws IOException {
        challengeTitle="1번 테스트 챌린지";
        challengeContents="1번 테스트 챌린지 입니다.";
        challengeStatus=true;
        challengeFrequency=2;
        challengeStartDate="2023-08-20";
        challengePeriod=4;
        challengeLanguage="Java";
        challengeSubject="개념 공부";
        challengePostType="Github";

        Path filePath = Paths.get("src/main/resources/static/images/example1.png");
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            file = new MockMultipartFile(
                    "example1.png",
                    "example1.png",
                    "image/png",
                    fileBytes
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        user3 = memberService.getByLoginId("user3");
    }

    @Test
    @DisplayName("각 회원은 한달에 한 번만 챌린지를 생성할 수 있다.")
    void t001 () throws IOException {

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user3);

//        RsData<Challenge> createRsData_1 = challengeService.createChallenge("1번 테스트 챌린지", "1번 테스트 챌린지 내용 입니다.",
//                true, 2, "2023-08-20", 2, "Java",
//                "개념 공부", "Github", file, user3);

        RsData<Challenge> createRsData_2 = challengeService.createChallenge("2번 테스트 챌린지", "2번 테스트 챌린지 내용",
                true, 3, "2023-06-20", 2, "C", "개념 공부", "GitHub", file, user3);

        //FIXME 스케줄로 Limit 초기화

        assertThat("S-1".equals(createRsData_1.getResultCode())).isTrue(); //첫 생성은 성공
        assertThat("F-1".equals(createRsData_2.getResultCode())).isTrue(); //두번째 생성은 Limit으로 인해 실패
    }

    @Test
    @DisplayName("챌린지 제목은 중복될 수 없다.")
    void t002 () throws IOException {

        Member user2 = memberService.getByLoginId("user2");

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user3);



        RsData<Challenge> createRsData_2 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user2);

        assertThat("S-1".equals(createRsData_1.getResultCode())).isTrue(); //"1번 테스트 챌린지" 생성
        assertThat("F-2".equals(createRsData_2.getResultCode())).isTrue(); //"1번 테스트 챌린지" 중복, 생성 실패
    }

    @Test
    @DisplayName("챌린지 제목은 24자를 넘어갈 수 없다.")
    void t003 () throws IOException {

        RsData<Challenge> createRsData_1 = challengeService.createChallenge("1번 테스트 챌린지 제목을 이렇게 길게 한다면 생성이 안될지도 몰라요",
                challengeContents, challengeStatus, challengeFrequency, challengeStartDate, challengePeriod,
                challengeLanguage, challengeSubject, challengePostType, file, user3);

        assertThat("F-3".equals(createRsData_1.getResultCode())).isTrue(); //제목이 길어 실패
    }

    @Test
    @DisplayName("챌린지 생성 날짜는 오늘 이후여야 한다")
    void t004 () throws IOException {

        String today = LocalDate.now().minusDays(1).toString();

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, today, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user3);

        assertThat("F-4".equals(createRsData_1.getResultCode())).isTrue(); //어제를 지정해서 생성이 불가
    }

    @Test
    @DisplayName("챌린지 내용은 500자를 넘을 수 없다.")
    void t005() throws IOException {

        String longText = "세글자".repeat(200); //총 600자

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, longText,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user3);

        assertThat("F-5".equals(createRsData_1.getResultCode())).isTrue(); //500자를 넘어 생성 불가
    }

    @Test
    @DisplayName("챌린지 생성시 파일 업로드는 아예 없거나, 이미지 파일만 가능하다.")
    void t006() throws IOException {
        
        MultipartFile txtFile = null;

        Path txtFilePath = Paths.get("src/main/resources/static/txt/testFile.txt");
        try {
            byte[] fileBytes = Files.readAllBytes(txtFilePath);
                txtFile = new MockMultipartFile(
                    "testFile.txt",
                    "testFile.txt",
                    "text/plain",
                    fileBytes
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        Member user4 = memberService.getByLoginId("user4");

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, txtFile, user3);

        MultipartFile emptyFile = new MockMultipartFile("filename", new byte[0]);
        
        RsData<Challenge> createRsData_2 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, emptyFile, user3);


        RsData<Challenge> createRsData_3 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user4);

        System.out.println("createRsData_1.getResultCode() = " + createRsData_1.getResultCode());
        System.out.println("createRsData_2.getResultCode() = " + createRsData_2.getResultCode());
        System.out.println("createRsData_3.getResultCode() = " + createRsData_3.getResultCode());

        assertThat("F-6".equals(createRsData_1.getResultCode())).isTrue(); //파일이 입력되었으나, 이미지가 아니면 생성 불가
        assertThat("S-1".equals(createRsData_2.getResultCode())).isTrue(); //파일이 없어도 생성 가능 (기본이미지로 생성)
        assertThat("S-6".equals(createRsData_3.getResultCode())).isTrue(); //정상적인 이미지를 넣으면 생성 가능
    }








}