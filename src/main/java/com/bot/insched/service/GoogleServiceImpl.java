package com.bot.insched.service;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.google.GoogleApiManager;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleServiceImpl implements GoogleService {
    @Autowired
    private GoogleApiManager manager;
    private MessageSender sender = MessageSender.getInstance();

    public String getAuthorizationUrl(String userId) {
        return manager.getAuthorizationUrl(userId);
    }

    public String authToken(String userId, String code) {
        boolean response = manager.authToken(userId, code);
        if (response) {
            String userInfo = getUserInfo(userId);
            sender.sendPrivateNotificationById("Welcome " + userInfo, userId);

            return "Succeed, Welcome " + userInfo
                + ", silakan kembali ke discord Anda.";
        } else {
            return "Sepertinya terdapat masalah, silakan coba kembali.";
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
