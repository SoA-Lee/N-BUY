package com.dalgorithm.nbuy.member.controller;

import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.dto.MemberFormDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.exception.MemberException;
import com.dalgorithm.nbuy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @RequestMapping("/members/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/members/register")
    public String register() {
        return "member/register";
    }

    @PostMapping("/members/register")
    public String registerSubmit(@Valid MemberFormDto memberFormDto,
                                 BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/register";
        }

        try{
            Member member = Member.createMember(memberFormDto);
            memberService.register(member);
            model.addAttribute("successMessage", "회원가입이 완료되었습니다. 이메일 인증을 해주시기 바랍니다.");
        } catch (MemberException e) {
            model.addAttribute("errorMessage", e.getErrorMessage());
            return "member/register";
        }

        return "index";
    }

    @GetMapping("/members/email_auth")
    public String emailAuth(HttpServletRequest request) {

        String uuid = request.getParameter("id");
        log.info(uuid);

        memberService.emailAuth(uuid);

        return "member/email_auth";
    }

    @GetMapping("/members/info")
    public String memberInfo(Model model, Principal principal) {

        String userId = principal.getName();

        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);

        return "member/info";
    }

    @PostMapping("/members/info")
    public String memberInfoSubmit(MemberDto memberDto, Principal principal) {

        String userId = principal.getName();
        memberDto.setUserId(userId);

        memberService.updateMember(memberDto);

        return "redirect:/members/info";
    }

    @GetMapping("/members/withdraw")
    public String memberWithdraw() {
        return "member/withdraw";
    }

    @PostMapping("/members/withdraw")
    public String memberWithdrawSubmit(Model model
            , MemberDto memberDto, Principal principal) {

        String userId = principal.getName();

        try{
            memberService.withdraw(userId, memberDto.getPassword());
            model.addAttribute("successMessage", "탈퇴가 완료되었습니다.");
        } catch (MemberException e) {
            model.addAttribute("errorMessage", e.getErrorMessage());
            return "common/error";
        }

        return "redirect:/members/logout";
    }

    // 코드 전체적인 정리
    // 탈퇴 기능하기
    // 관리자 로그인 시 메인화면 다르게 보이기 springSecurity 설정
}
