package com.dalgorithm.nbuy.admin.controller;

import com.dalgorithm.nbuy.admin.entity.MemberParam;
import com.dalgorithm.nbuy.member.dto.MemberDto;
import com.dalgorithm.nbuy.member.entity.Member;
import com.dalgorithm.nbuy.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public String searchMember(Model model, String keyword, @PageableDefault(size = 5, sort = "userId",
            direction = Sort.Direction.DESC) Pageable pageable) {

        Page<MemberDto> memberList = memberService.searchMember(keyword, pageable);

        int startPage = Math.max(1, memberList.getPageable().getPageNumber() - 4);
        int endPage = Math.min(memberList.getPageable().getPageNumber()+4, memberList.getTotalPages());

        model.addAttribute("keyword", keyword);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("memberList", memberList);

        return "admin/member/list";
    }

    @GetMapping("/detail")
    public String memberDetail(Model model, MemberParam memberParam) {

        MemberDto member = memberService.detail(memberParam.getUserId());
        model.addAttribute("member", member);

        return "admin/member/detail";
    }

    @PostMapping("/status")
    public String updateMemberStatus(MemberDto memberDto) {

        memberService.updateStatus(memberDto.getUserId(), memberDto.getUserStatus());

        return "redirect:/admin/members/detail?userId=" + memberDto.getUserId();
    }
}
