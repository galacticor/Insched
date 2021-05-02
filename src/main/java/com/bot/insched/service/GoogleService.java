package com.bot.insched.service;

public interface GoogleService {

	String getAuthorizationUrl();

	String authToken(String userId, String code);
}
