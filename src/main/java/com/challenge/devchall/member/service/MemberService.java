package com.challenge.devchall.member.service;

import ch.qos.logback.core.spi.ConfigurationEvent;
import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import com.challenge.devchall.point.entity.Point;
import com.challenge.devchall.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.awt.datatransfer.Clipboard;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final PointService pointService;
    public Optional<Member> findByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID);
    }

    @Transactional
    public RsData<Member> join(String loginID, String password, String email, String nickname, String username) {

        RsData<Member> rsData = validateMember(loginID, email, nickname);

        if (rsData.isFail()) return rsData;
        Member member = Member
                .builder()
                .loginID(loginID)
                .email(email)
                .nickname(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .challengeLimit(0)
                .point(pointService.create())
                .build();
        memberRepository.save(member);
        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }
    private RsData<Member> join(String providerTypeCode, String loginID, String password){
        if (findByLoginID(loginID).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(loginID));
        }
        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .loginID(loginID)
                .nickname(loginID)
                .password(password)
                .challengeLimit(0)
                .point(pointService.create())
                .build();
        memberRepository.save(member);
        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }


    public RsData<Member> validateMember (String loginID, String email, String nickname) {
        if (findByLoginID(loginID).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(loginID));
        }
        if (memberRepository.existsByNickname(nickname)){
            return RsData.of("F-2", "해당 닉네임은(%s)는 이미 사용중입니다. 다른 닉네임을 사용해주세요.".formatted(nickname));
        }
        if (memberRepository.existsByEmail(email)){
            return RsData.of("F-3", "해당 이메일은 이미 사용중입니다.");
        }
        return RsData.of("S-1", "유효성 검사 완료");
    }


    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    // 소셜 로그인(카카오, 구글, 네이버) 로그인이 될 때 마다 실행되는 함수
    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String loginID) {
        Optional<Member> opMember = findByLoginID(loginID); // socialId 예시 : KAKAO__1312319038130912, NAVER__1230812300

        if (opMember.isPresent()) return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        // 소셜 로그인를 통한 가입시 비번은 없다.
        return join(providerTypeCode, loginID, ""); // 최초 로그인 시 딱 한번 실행
    }


    public Member getByLoginId(String loginID){

        return memberRepository.findByLoginID(loginID).orElse(null);
    }

    public RsData<Member> checkChallengeLimit(Member member){

        int challengeLimit = member.getChallengeLimit();

        if(challengeLimit < 2){
            member.setChallengeLimit(challengeLimit + 1);

            return RsData.of("S-1", "챌린지 개설이 가능합니다.");
        }else{
            return RsData.of("F-1", "이미 이번달에 2개의 챌린지를 생성하셨습니다.");
        }
    }

    public RsData<Member> canJoin(Member member, int joinCost){

        Point memberPoint = member.getPoint();
        Long currentPoint = memberPoint.getCurrentPoint();

        if(currentPoint >= joinCost){

            memberPoint.subtract(joinCost);

            return RsData.of("S-1", "참가 비용을 지불할 수 있습니다");
        }else{
            return RsData.of("F-3", "참가 비용이 부족합니다.");
        }
    }

}