package com.dalgorithm.nbuy.order.controller;

import com.dalgorithm.nbuy.order.entity.OrderInput;
import com.dalgorithm.nbuy.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/request")
    public String courseReq(Model model, @RequestBody OrderInput parameter
            , Principal principal) {

        parameter.setApplicantId(principal.getName());
        orderService.reqPurchase(parameter);

        model.addAttribute("successMessage", "같이구매 신청이 완료되었습니다. 마이페이지를 확인해주세요.");
        return "index";
    }
}
