package com.bot.insched.controller;

import com.bot.insched.service.GoogleService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/")
public class MainController {
    @Autowired
    private GoogleService service;

    @GetMapping(path = "auth", produces = {"application/json"})
    @ResponseBody
    public String authToken(@RequestParam String state, @RequestParam String code) {
        String response = service.authToken(state, code);
        return response;
    }

    @GetMapping(path = "ping", produces = {"application/json"})
    @ResponseBody
    public String healthCheck() {
        return "PONG";
    }
}
