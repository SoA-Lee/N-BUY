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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @RequestMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/register")
    public String register() {
        return "member/register";
    }

    @PostMapping("/register")
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

    @GetMapping("/email_auth")
    public String emailAuth(HttpServletRequest request) {

        String uuid = request.getParameter("id");
        log.info(uuid);

        memberService.emailAuth(uuid);

        return "member/email_auth";
    }

    @GetMapping("/info")
    public String memberInfo(Model model, Principal principal) {

        String userId = principal.getName();

        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);

        return "member/info";
    }

    @PostMapping("/info")
    public String memberInfoSubmit(MemberDto memberDto, Principal principal) {

        String userId = principal.getName();
        memberDto.setUserId(userId);

        memberService.updateMember(memberDto);

        return "redirect:/members/info";
    }

    @GetMapping("/withdraw")
    public String memberWithdraw() {
        return "member/withdraw";
    }

    @PostMapping("/withdraw")
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
}
