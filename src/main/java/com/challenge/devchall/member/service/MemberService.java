package com.challenge.devchall.member.service;

import com.challenge.devchall.base.rsData.RsData;
import com.challenge.devchall.item.repository.ItemRepository;
import com.challenge.devchall.member.entity.Member;
import com.challenge.devchall.member.repository.MemberRepository;
import com.challenge.devchall.point.entity.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import com.challenge.devchall.item.entity.Item;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Optional<Member> findByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID); //findByLoginID??
    }

    @Transactional
    public RsData<Member> join(String loginID, String password, String email, String nickname, String username) {
        RsData<Member> rsData = validateMember(loginID, email, nickname);
        Point point = new Point(1000L, 1000L);

        if (rsData.isFail()) return rsData;
        Member member = Member
                .builder()
                .loginID(loginID)
                .email(email)
                .nickname(nickname)
                .username(username)
                .password(passwordEncoder.encode(password))
                .challengeLimit(0)
                .point(point)
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

    public RsData<Member> updateChallengeLimit(Member member){

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

            memberPoint.updateCurrentPoint((joinCost * -1));

            return RsData.of("S-1", "참가 비용을 지불할 수 있습니다");
        }else{
            return RsData.of("F-3", "참가 비용이 부족합니다.");
        }
    }


    @Transactional
    public RsData<Member> buyItem(String buyCode, Member member){

        /*
        가격, 아이템 구매 코드 유형
        (1) L or H (싼 것, 비싼것)
        (2) F or C (폰트, 캐릭터)
        (3) 색상코드 or 캐릭터명
        ex) L-F-358544 (가격이 싼 폰트 358544에 대한 구매요청코드)
        */

        String[] buyCodeSplit = buyCode.split("-");

        System.out.println("buyCodeSplit = " + Arrays.toString(buyCodeSplit));

        int itemCost = getItemCost(buyCodeSplit[0], buyCodeSplit[1]);

        Point memberPoint = member.getPoint();

        if(memberPoint.getCurrentPoint() >= itemCost){

            //포인트 차감
            member.getPoint().updateCurrentPoint(itemCost * -1);

            //FIXME 구매한 아이템이 들어오도록 하는 것 짜야함. + 실제 적용

            Item purchasedItem = Item
                    .builder()
                    .name(buyCodeSplit[2])
                    .type(buyCodeSplit[1])
                    .member(member)
                    .build();

            itemRepository.save(purchasedItem);


            // 아이템을 멤버에게 추가
            member.setPurchasedItem(purchasedItem, member);

            return RsData.of("S-6", "구매에 성공하였습니다.");

        }else {

            return RsData.of("F-6", "소지금이 부족합니다.");
        }

    }

    public int getItemCost(String costType, String itemType) {
        if (costType.equals("L")) {
            if (itemType.equals("F")) {
                return 300;
            } else if (itemType.equals("C")) {
                return 1000;
            }
        } else if (costType.equals("H")) {
            if (itemType.equals("F")) {
                return 500;
            } else if (itemType.equals("C")) {
                return 2000;
            }
        }

        // 예외 처리
        throw new IllegalArgumentException("유효하지 않은 costType 또는 itemType입니다.");
    }


    public int getMemberPoint(String loginId) {
        Member member = getByLoginId(loginId);

        if (member != null) {
            Point point = member.getPoint();
            if (point != null) {
                return point.getCurrentPoint().intValue();
            }
        }
        return 0; // 멤버가 존재하지 않거나 포인트 정보가 없는 경우, 0을 반환
    }

}