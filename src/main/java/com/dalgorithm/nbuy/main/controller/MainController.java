package com.dalgorithm.nbuy.main.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @ApiOperation(value = "메인 화면으로 이동하기")
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @ApiOperation(value = "에러 화면으로 이동하기")
    @RequestMapping("/error/denied")
    public String errorDenied() {
        return "common/denied";
    }
}
