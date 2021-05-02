package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

@Component
public class GoogleServiceImpl implements GoogleService{
	// @Autowired
	private GoogleAPIManager manager;

	public GoogleServiceImpl(){
		manager = new GoogleAPIManager();
	}

	public String getAuthorizationUrl() {
	  	return manager.getAuthorizationUrl();
	}
}
