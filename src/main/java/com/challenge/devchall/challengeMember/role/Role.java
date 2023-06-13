package com.challenge.devchall.challengeMember.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    LEADER("챌린지 리더"), CREW("챌린지 팀원");
    private final String title;
}
