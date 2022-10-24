package com.dalgorithm.nbuy.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied() {
        return "error/denied";
    }
}
