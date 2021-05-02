package com.bot.insched.google;

import com.bot.insched.google.credentials.JPADataStore;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleAPIManager {
	private static final String APPLICATION_NAME = "Insched (Instant Scheduler)";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CLIENT_SECRET = "oksfFIk_VzNxb9G08Iup7_1U";
    private static final String CLIENT_ID = "38754712208-el2lrejbff3mineg0sc0cbiimiqbj349.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	private HttpTransport transport;

	// public GoogleAPIManager() throws IOException {
	// 	InputStream in = GoogleAPIManager.class.getResourceAsStream(CLIENT_SECRET_FILE);
	// 	clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	// }

	public String getAuthorizationUrl() {
		GoogleAuthorizationCodeRequestUrl urlBuilder =
		    new GoogleAuthorizationCodeRequestUrl( CLIENT_ID, REDIRECT_URI, SCOPES)
		    .setAccessType("offline");
	  	return urlBuilder.build();
	}
}