package com.dalgorithm.nbuy.member.controller;

import com.dalgorithm.nbuy.exception.AbstractException;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.dto.MemberFormDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.service.MemberService;
import com.dalgorithm.nbuy.order.dto.OrderDto;
import com.dalgorithm.nbuy.order.service.OrderService;
import com.dalgorithm.nbuy.product.dto.ProductDto;
import com.dalgorithm.nbuy.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final ProductService productService;

    @ApiOperation(value = "로그인하기")
    @RequestMapping("/login")
    public String login() {
        return "member/login";
    }

    @ApiOperation(value = "회원가입 페이지 요청하기")
    @GetMapping("/register")
    public String register() {
        return "member/register";
    }

    @ApiOperation(value = "회원가입하기", notes = "MemberDto")
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
        } catch (AbstractException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/register";
        }

        return "index";
    }

    @ApiOperation(value = "이메일 인증하기")
    @GetMapping("/email_auth")
    public String emailAuth(HttpServletRequest request) {

        String uuid = request.getParameter("id");
        log.info(uuid);

        memberService.emailAuth(uuid);

        return "member/email_auth";
    }

    @ApiOperation(value = "마이페이지 요청하기")
    @GetMapping("/info")
    public String memberInfo(Model model, Principal principal) {

        String userId = principal.getName();

        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);

        return "member/info";
    }
    @ApiOperation(value = "마이페이지 - 회원 정보 수정하기", notes = "MemberDto")

    @PostMapping("/info")
    public String memberInfoSubmit(MemberDto memberDto, Principal principal) {

        String userId = principal.getName();
        memberDto.setUserId(userId);

        memberService.updateMember(memberDto);

        return "redirect:/members/info";
    }

    @ApiOperation(value = "탈퇴 페이지 요청하기")
    @GetMapping("/withdraw")
    public String memberWithdraw() {
        return "member/withdraw";
    }

    @ApiOperation(value = "탈퇴하기")
    @PostMapping("/withdraw")
    public String memberWithdrawSubmit(Model model
            , MemberDto memberDto, Principal principal) {

        String userId = principal.getName();

        try{
            memberService.withdraw(userId, memberDto.getPassword());
            model.addAttribute("successMessage", "탈퇴가 완료되었습니다.");
        } catch (AbstractException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "common/error";
        }

        return "redirect:/members/logout";
    }

    @ApiOperation(value = "마이페이지 - 모집 상태 확인하기")
    @GetMapping("/recruit_status")
    public String checkMyRecruit(Model model, Principal principal) {
        String recruiterId = principal.getName();
        List<ProductDto> list = productService.myRecruit(recruiterId);
        model.addAttribute("list", list);

        return "member/my_recruit";
    }

    @ApiOperation(value = "마이페이지 - 주문 상태 확인하기")
    @GetMapping("/apply_status")
    public String checkMyApply(Model model, Principal principal) {
        String applicantId = principal.getName();
        List<OrderDto> list = orderService.myApply(applicantId);

        model.addAttribute("list", list);
        return "member/my_apply";
    }
}
