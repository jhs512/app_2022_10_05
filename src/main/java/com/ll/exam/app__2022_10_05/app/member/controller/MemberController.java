package com.ll.exam.app__2022_10_05.app.member.controller;

import com.ll.exam.app__2022_10_05.app.base.dto.RsData;
import com.ll.exam.app__2022_10_05.app.member.entity.Member;
import com.ll.exam.app__2022_10_05.app.member.request.LoginRequest;
import com.ll.exam.app__2022_10_05.app.member.service.MemberService;
import com.ll.exam.app__2022_10_05.app.security.entity.MemberContext;
import com.ll.exam.app__2022_10_05.util.Util;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal MemberContext memberContext) {
        return "안녕" + memberContext;
    }

    @GetMapping("/me")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext) {
        return Util.spring.responseEntityOf(RsData.of("S-1", "성공", memberContext));
    }

    @PostMapping("/login")
    public ResponseEntity<RsData> login(@Valid @RequestBody LoginRequest loginRequest) {
        Member member = memberService.findByUsername(loginRequest.getUsername()).orElse(null);

        if (member == null) {
            return Util.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()) == false) {
            return Util.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        log.debug("Util.json.toStr(member.getAccessTokenClaims()) : " + Util.json.toStr(member.getAccessTokenClaims()));

        String accessToken = memberService.genAccessToken(member);

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Util.mapOf(
                                "accessToken", accessToken
                        )
                ),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }


}
