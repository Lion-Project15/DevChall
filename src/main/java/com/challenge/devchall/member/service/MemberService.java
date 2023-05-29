package com.challenge.devchall.member.service;

import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Member join(String loginID, String password, String email, String nickname, String username) {
        Member member = Member
                .builder()
                .loginID(loginID)
                .email(email)
                .nickname(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return memberRepository.save(member);
    }
}