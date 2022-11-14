package com.dalgorithm.nbuy.order.controller;

import com.dalgorithm.nbuy.order.entity.OrderInput;
import com.dalgorithm.nbuy.order.service.OrderService;
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

    @PostMapping("/request")
    public String orderReq(@RequestBody OrderInput parameter
            , Principal principal) {

        parameter.setApplicantId(principal.getName());
        orderService.reqPurchase(parameter);

        return "index";
    }

    @PostMapping("/cancel")
    public String orderCancel(Model model
            , @RequestBody OrderInput parameter
            , Principal principal) {

        parameter.setApplicantId(principal.getName());
        orderService.reqPurchase(parameter);

        String userId = principal.getName();


        return "redirect:/members/apply_status";
    }
}
