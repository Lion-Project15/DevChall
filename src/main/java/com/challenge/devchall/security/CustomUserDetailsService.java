package com.challenge.devchall.security;

import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String loginID) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginID(loginID).orElseThrow(()
                -> new UsernameNotFoundException("loginID(%s) not found".formatted(loginID)));

        return new User(member.getLoginID(), member.getPassword(), member.getGrantedAuthorities());
    }
}
