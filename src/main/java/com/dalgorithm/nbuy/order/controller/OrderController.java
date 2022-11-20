package com.dalgorithm.nbuy.order.controller;

import com.dalgorithm.nbuy.order.entity.OrderInput;
import com.dalgorithm.nbuy.order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation(value = "같이구매 주문 신청하기")
    @PostMapping("/request")
    public String orderReq(@RequestBody OrderInput parameter
            , Principal principal) {

        parameter.setApplicantId(principal.getName());
        System.out.println(parameter.getProductId());
        orderService.reqPurchase(parameter);

        return "index";
    }

    @ApiOperation(value = "같이구매 주문 취소하기")
    @PostMapping("/cancel")
    public String orderCancel(@RequestBody OrderInput parameter
            , Principal principal) {

        parameter.setApplicantId(principal.getName());
        System.out.println(parameter.getProductId());
        orderService.cancelPurchase(parameter);

        return "redirect:/members/apply_status";
    }
}
