package com.bot.insched.service;

public interface GoogleService {

    String getAuthorizationUrl(String userId);

    String authToken(String userId, String code);

    String getUserInfo(String userId);
}
