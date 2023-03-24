package com.gp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppControlller {

    @GetMapping
    String a() {
        return "main_page";
    }
}
