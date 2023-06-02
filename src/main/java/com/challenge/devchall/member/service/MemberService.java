package com.challenge.devchall.member.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    public Optional<Member> findByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID); //findByLoginID??
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

    public Member getByLoginId(String loginId){

        return memberRepository.findByLoginID(loginId).orElse(null);
    }
}