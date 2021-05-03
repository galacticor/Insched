package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleServiceImpl implements GoogleService{
	@Autowired
	private GoogleAPIManager manager;

	public String getAuthorizationUrl() {
	  	return manager.getAuthorizationUrl();
	}

	public String authToken(String userId, String code) {
		boolean response = manager.authToken(userId, code);
		if (response) {
			return "Succeed";
		} else {
			return "Failed";
		}
	}
}
