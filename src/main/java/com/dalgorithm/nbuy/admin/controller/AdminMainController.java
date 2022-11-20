package com.dalgorithm.nbuy.admin.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMainController {

    @ApiOperation(value = "관리자 - 메인화면으로 이동하기")
    @GetMapping("/admin/main")
    public String main() {

        return "/index";

    }
}
