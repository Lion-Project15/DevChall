package com.challenge.devchall.challange.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.challange.entity.Challenge;
import com.challenge.devchall.challange.repository.ChallengeRepository;
import com.challenge.devchall.challengeMember.role.Role;
import com.challenge.devchall.challengeMember.service.ChallengeMemberService;
import com.challenge.devchall.challengepost.service.ChallengePostService;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.service.MemberService;
import com.challenge.devchall.photo.service.PhotoService;
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
import java.util.List;

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
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private PhotoService photoService;

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
        challengeTitle = "1번 테스트 챌린지";
        challengeContents = "1번 테스트 챌린지 입니다.";
        challengeStatus = true;
        challengeFrequency = 2;
        challengeStartDate = "2023-08-20";
        challengePeriod = 4;
        challengeLanguage = "Java";
        challengeSubject = "개념 공부";
        challengePostType = "Github";

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
    void t001() throws IOException {

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user3);

        RsData<Challenge> createRsData_2 = challengeService.createChallenge("2번 테스트 챌린지", "2번 테스트 챌린지 내용",
                true, 3, "2023-06-20", 2, "C", "개념 공부", "GitHub", file, user3);

        //FIXME 스케줄로 Limit 초기화

        System.out.println("createRsData_2.getMsg() = " + createRsData_2.getMsg());
        System.out.println("createRsData_2.getResultCode() = " + createRsData_2.getResultCode());

        assertThat("S-1".equals(createRsData_1.getResultCode())).isTrue(); //첫 생성은 성공
        assertThat("F-1".equals(createRsData_2.getResultCode())).isTrue(); //두번째 생성은 Limit으로 인해 실패
    }

    @Test
    @DisplayName("챌린지 제목은 중복될 수 없다.")
    void t002() throws IOException {

        Member user4 = memberService.getByLoginId("user4");

        RsData<Challenge> createRsData_1 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user3);

        RsData<Challenge> createRsData_2 = challengeService.createChallenge(challengeTitle, challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user4);

        assertThat("S-1".equals(createRsData_1.getResultCode())).isTrue(); //"1번 테스트 챌린지" 생성
        assertThat("F-2".equals(createRsData_2.getResultCode())).isTrue(); //"1번 테스트 챌린지" 중복, 생성 실패
    }

    @Test
    @DisplayName("챌린지 제목은 24자를 넘어갈 수 없다.")
    void t003() throws IOException {

        RsData<Challenge> createRsData_1 = challengeService.createChallenge("1번 테스트 챌린지 제목을 이렇게 길게 한다면 생성이 안될지도 몰라요",
                challengeContents, challengeStatus, challengeFrequency, challengeStartDate, challengePeriod,
                challengeLanguage, challengeSubject, challengePostType, file, user3);

        assertThat("F-3".equals(createRsData_1.getResultCode())).isTrue(); //제목이 길어 실패
    }

    @Test
    @DisplayName("챌린지 생성 날짜는 오늘 이후여야 한다")
    void t004() throws IOException {

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

        Member user6 = memberService.getByLoginId("user6");

        RsData<Challenge> createRsData_1 = challengeService.createChallenge("이미지 파일이 아니면 생성 불가", challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, txtFile, user3);

        MultipartFile emptyFile = new MockMultipartFile("filename", new byte[0]);

        RsData<Challenge> createRsData_2 = challengeService.createChallenge("기본 이미지 챌린지", challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, emptyFile, user3);


        RsData<Challenge> createRsData_3 = challengeService.createChallenge("이미지가 있는 챌린지", challengeContents,
                challengeStatus, challengeFrequency, challengeStartDate, challengePeriod, challengeLanguage,
                challengeSubject, challengePostType, file, user6);

        RsData<String> isImg = photoService.isImgFile(file.getOriginalFilename());
        RsData<String> isImg2 = photoService.isImgFile(emptyFile.getOriginalFilename());
        RsData<String> isImg3 = photoService.isImgFile(txtFile.getOriginalFilename());

        //S-6 : 이미지 파일이 맞음, S-7 : 파일이 비었다면 기본 이미지로 생성, F-6 : 파일이 들어왔으나 이미지가 아니라 생성 불가
        assertThat("S-6".equals(isImg.getResultCode())).isTrue();
        assertThat("S-7".equals(isImg2.getResultCode())).isTrue();
        assertThat("F-6".equals(isImg3.getResultCode())).isTrue();

        assertThat("F-6".equals(createRsData_1.getResultCode())).isTrue();
        assertThat("S-1".equals(createRsData_2.getResultCode())).isTrue();
        assertThat("S-1".equals(createRsData_3.getResultCode())).isTrue();
    }

    @Test
    @DisplayName("getChallengeList(String language, String subject)는 필터린 된 결과 중 공개여부가 true 인 챌린지를 모두 가져옴")
    void t007() throws IOException {

        Member user4 = memberService.getByLoginId("user4");
        Member user6 = memberService.getByLoginId("user6");

        challengeService.createChallenge("테스트자바, 테스트개념1", challengeContents,
                true, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user3);

        challengeService.createChallenge("테스트 C, 테스트 개념2", challengeContents,
                true, challengeFrequency, challengeStartDate, challengePeriod, "testC",
                "개념 공부", challengePostType, file, user4);

        challengeService.createChallenge("테스트자바, 테스트개념2", challengeContents,
                false, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user6);

        boolean testToken = true;

        List<Challenge> challengeList = challengeService.getChallengeList("testJava", "개념 공부");

        for (Challenge challenge : challengeList) {

            if (!challenge.getChallengeTag().getChallengeLanguage().equals("testJava") &&
                    !challenge.getChallengeTag().getChallengeSubject().equals("개념 공부")) {

                testToken = false;
                break;
            }
        }

        assertThat(challengeList.size()).isEqualTo(1); //필터링 결과는 2지만, false 가 하나 있어 1개가 반환
        assertThat(testToken).isTrue(); //찾은 내용중에 잘못된 것은 없었음.
    }

    @Test
    @DisplayName("getNotJoinChallengeList 메서드는 필터링 결과에서 내가 참여하지 않은 챌린지 중 공개 여부가 true 인 모든 챌린지를 가져온다.")
    void t008() throws IOException {

        Member user4 = memberService.getByLoginId("user4");
        Member user6 = memberService.getByLoginId("user6");

        challengeService.createChallenge("테스트자바, 테스트개념1", challengeContents,
                true, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user3);

        challengeService.createChallenge("테스트자바, 테스트개념2", challengeContents,
                true, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user4);

        challengeService.createChallenge("테스트자바, 테스트개념3", challengeContents,
                false, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user6);

        Challenge challenge1 = challengeRepository.findByChallengeName("테스트자바, 테스트개념1").get();

        challengeMemberService.addMember(challenge1, user6, Role.CREW);

        //user6이 나라고 했을 때, 3번은 내가 만들었고, 1번은 참여했으므로 2번만 결과로 나와야 한다.
        List<Challenge> notJoinChallengeList = challengeService.getNotJoinChallengeList("testJava", "개념 공부", user6);

        boolean testToken = true;

        for (Challenge challenge : notJoinChallengeList) {

            if (!challenge.getChallengeTag().getChallengeLanguage().equals("testJava") &&
                    !challenge.getChallengeTag().getChallengeSubject().equals("개념 공부")) {

                testToken = false;
                break;
            }
        }

        assertThat(testToken).isTrue();
        assertThat(notJoinChallengeList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("getJoinChallengeList 메서드는 공개 여부에 상관없이 내가 참여한 모든 챌린지를 가져온다.")
    void t009() throws IOException {

        Member user4 = memberService.getByLoginId("user4");
        Member user6 = memberService.getByLoginId("user6");

        challengeService.createChallenge("테스트자바, 테스트개념1", challengeContents,
                true, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user3);

        challengeService.createChallenge("테스트자바, 테스트개념2", challengeContents,
                true, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user4);

        challengeService.createChallenge("테스트자바, 테스트개념3", challengeContents,
                false, challengeFrequency, challengeStartDate, challengePeriod, "testJava",
                "개념 공부", challengePostType, file, user6);

        Challenge challenge1 = challengeRepository.findByChallengeName("테스트자바, 테스트개념1").get();
        Challenge challenge2 = challengeRepository.findByChallengeName("테스트자바, 테스트개념2").get();

        challengeMemberService.addMember(challenge1, user6, Role.CREW);
        challengeMemberService.addMember(challenge2, user6, Role.CREW);

        //하나는 생성, 두개는 참여했으므로 총 3개가 나와야 한다.
        assertThat(challengeService.getJoinChallenge(user6).size()).isEqualTo(3);
    }
}
