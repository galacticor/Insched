package com.bot.insched.google;

import com.bot.insched.google.credentials.JPADataStore;
import com.bot.insched.google.credentials.JPADataStoreFactory;
import com.bot.insched.repository.DiscordUserRepository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfoplus;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class GoogleAPIManager {
	private static final String APPLICATION_NAME = "Insched (Instant Scheduler)";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR, Oauth2Scopes.USERINFO_EMAIL);

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
			log.error("------ Error when configurate");
		}
		
	}

	public String getAuthorizationUrl() {
		String redirectTo = flow.newAuthorizationUrl()
				.setRedirectUri(REDIRECT_URI)
				.build();
	  	return redirectTo;
	}

	public boolean authToken(String userId, String code) {
		try {
			GoogleTokenResponse googleResponse = flow.newTokenRequest(code)
					.setRedirectUri(REDIRECT_URI)
					.execute();

			Credential cred = flow.createAndStoreCredential(googleResponse, userId);

			return true;
		} catch (Exception e){
			log.warn("------ Error when save credential for user {} : {}", userId, e);
			return false;
		}
	}

	public GoogleCredential getCredential(String userId) {
		try{
			Credential storedCredential = flow.loadCredential(userId);
			if (storedCredential == null) {
				log.warn("------ Couldn't retrieve Google credential for user {}", userId);
				return null;
			}
			GoogleCredential credential = new GoogleCredential.Builder()
					.setTransport(httpTransport)
					.setJsonFactory(JSON_FACTORY)
					.setClientSecrets(CLIENT_ID, CLIENT_SECRET)
					.addRefreshListener(new CredentialRefreshListener() {
						public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) {
							log.error("OAuth token refresh error: {}", tokenErrorResponse);
						}

						public void onTokenResponse(Credential credential, TokenResponse tokenResponse) {
							log.debug("OAuth token was refreshed");
						}
					})
					.addRefreshListener(new DataStoreCredentialRefreshListener(userId, flow.getCredentialDataStore()))
					.build();

			credential.setRefreshToken(storedCredential.getRefreshToken());
			credential.setAccessToken(storedCredential.getAccessToken());
			log.debug("Credential: Get Credential for user : {}", credential.getServiceAccountUser());

			return credential;
		} catch (Exception e) {
			log.warn("Credential: Error while refreshing or saving the token for user {} : {}", userId, e.getMessage());
		}

		return null;
	}

	public Calendar getCalendarService (String userId){
		GoogleCredential credential = getCredential(userId);
		if (credential == null) {
			return null;
		}

		Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
	            .setApplicationName(APPLICATION_NAME)
	            .build();

	    return service;
	}

	public Oauth2 getOauth2Service (String userId) {
		GoogleCredential credential = getCredential(userId);
		Oauth2 service = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName("Oauth2")
				.build();

		return service;
	}

	public Userinfoplus getUserInfo(String userId) {
		Oauth2 oauth2 = getOauth2Service(userId);
		try {
			Userinfoplus userinfo = oauth2.userinfo().get().execute();	
			return userinfo;
		} catch (Exception e){
			log.warn("Userinfo: Error while execute userinfo {} : {}", userId, e.getMessage());
		}
		
		return null;
	}

}