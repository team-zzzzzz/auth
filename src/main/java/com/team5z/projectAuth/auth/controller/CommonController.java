package com.team5z.projectAuth.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class CommonController {
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

}
