package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;

import com.google.api.services.oauth2.model.Userinfoplus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class GoogleServiceImplTest {
    @Mock
    private GoogleAPIManager manager;

    @InjectMocks
    GoogleServiceImpl service;

    private String authUrl = "authUrl";;
    private String dummyId = "123";
    private String dummyCode = "123";
    private String email = "a@a.com";
    private Userinfoplus userinfo;

    @BeforeEach
    public void setUp() {
        userinfo = new Userinfoplus();
        userinfo.setEmail(email);
    }

    @Test
    public void testGetAuthUrl() {
        when(manager.getAuthorizationUrl()).thenReturn(authUrl);
        String url = service.getAuthorizationUrl();

        assertEquals(url, authUrl);
    }

    @Test
    public void testAuthTokenSuccess() {
        when(manager.authToken(dummyId, dummyCode)).thenReturn(true);
        assertNotEquals(service.authToken(dummyId, dummyCode), "Failed");
    }

    @Test
    public void testAuthTokenFailed() {
        when(manager.authToken(dummyId, dummyCode)).thenReturn(false);
        assertEquals(service.authToken(dummyId, dummyCode), "Failed");
    }

    @Test
    public void testGetUserInfoSuccess() {
        when(manager.getUserInfo(dummyId)).thenReturn(userinfo);
        assertEquals(service.getUserInfo(dummyId), email);
    }

    @Test
    public void testGetUserInfoFailed() {
        when(manager.getUserInfo(dummyId)).thenReturn(null);
        assertEquals(service.getUserInfo(dummyId), "");
    }
}