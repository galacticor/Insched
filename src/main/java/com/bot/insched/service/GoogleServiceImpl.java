package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.oauth2.model.Userinfoplus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleServiceImpl implements GoogleService {
    @Autowired
    private GoogleAPIManager manager;

    public String getAuthorizationUrl() {
        return manager.getAuthorizationUrl();
    }

    public String authToken(String userId, String code) {
        boolean response = manager.authToken(userId, code);
        if (response) {
            return "Succed, Welcome " + getUserInfo(userId);
        } else {
            return "Failed";
        }
    }

    public String getUserInfo(String userId) {
        Userinfoplus userinfo = manager.getUserInfo(userId);
        if (userinfo == null) {
            return "";
        }

        return userinfo.getEmail();

    }
}
