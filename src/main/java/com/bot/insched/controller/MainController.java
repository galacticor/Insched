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

	@GetMapping(path = "auth")
	@ResponseBody
	public String authToken(@RequestParam String id, @RequestParam String code){
		String response = service.authToken(id, code);

		System.out.println(id);
		System.out.println(code);
		if (response.equals("Failed")){
			return "Gagal, silakan coba kembali.";	
		}
		return "Sukses, silakan kembali ke discord Anda.";
	}
}
