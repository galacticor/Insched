package com.bot.insched.google;

import com.bot.insched.google.credentials.JPADataStore;
import com.bot.insched.google.credentials.JPADataStoreFactory;
import com.bot.insched.repository.DiscordUserRepository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class GoogleAPIManager {
	private static final String APPLICATION_NAME = "Insched (Instant Scheduler)";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private static final String CLIENT_SECRET = "oksfFIk_VzNxb9G08Iup7_1U";
    private static final String CLIENT_ID = "38754712208-el2lrejbff3mineg0sc0cbiimiqbj349.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	@Setter
	@Getter
	private DiscordUserRepository userRepo;

	private GoogleAuthorizationCodeFlow flow;
	private HttpTransport httpTransport;

	@Autowired
	public GoogleAPIManager(
			DiscordUserRepository repository
		){
		this.userRepo = repository;
		init();
	}

	public void init(){	
		try {
			// userRepo = new DiscordUserRepository();
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			DataStoreFactory dataStore = new JPADataStoreFactory(userRepo);			
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
					.setDataStoreFactory(dataStore)
					.setApprovalPrompt("force")
					.setAccessType("offline")
					.build();	
		} catch (Exception e){
			log.error("Error when configurate");
		}
		
	}

	public String getAuthorizationUrl() {
		String redirectTo = flow.newAuthorizationUrl()
				.setRedirectUri(REDIRECT_URI)
				.build();
		// GoogleAuthorizationCodeRequestUrl urlBuilder =
		//     new GoogleAuthorizationCodeRequestUrl( CLIENT_ID, REDIRECT_URI, SCOPES)
		//     .setAccessType("offline");
	  	return redirectTo;
	}

	public boolean authToken(String userId, String code) {
		try {
			// GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
   //            httpTransport,
   //            JSON_FACTORY,
   //            "https://oauth2.googleapis.com/token",
   //            CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI).execute();
			GoogleTokenResponse googleResponse = flow.newTokenRequest(code)
					.setRedirectUri(REDIRECT_URI)
					.execute();

			Credential cred = flow.createAndStoreCredential(googleResponse, userId);

			return true;
		} catch (Exception e){
			log.warn("------Error when save credential for user {} : {}", userId, e);
			return false;
		}
		
	}


}