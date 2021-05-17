package com.bot.insched.google;

import com.bot.insched.google.credentials.JPADataStoreFactory;
import com.bot.insched.repository.DiscordUserRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfoplus;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class GoogleApiManager {
    private static final String APPLICATION_NAME = "Insched (Instant Scheduler)";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES =
        Arrays.asList(CalendarScopes.CALENDAR, Oauth2Scopes.USERINFO_EMAIL);

    private static String CLIENT_SECRET;
    private static String CLIENT_ID;
    private static String REDIRECT_URI;

    private DiscordUserRepository userRepo;
    private Builder builder;

    private GoogleAuthorizationCodeFlow flow;
    private HttpTransport httpTransport;

        @Autowired
        public GoogleApiManager(
            DiscordUserRepository repository,
            Builder builder,
            @Value("${client_id}") String clientId,
            @Value("${client_secret}") String clientSecret,
            @Value("${redirect_uri}") String redirectUri) {
            this.userRepo = repository;
            this.builder = builder;
            CLIENT_ID = clientId;
            CLIENT_SECRET = clientSecret;
            REDIRECT_URI = redirectUri;
            init();
        }

//    @Autowired
//    public GoogleApiManager(
//        DiscordUserRepository repository,
//        Builder builder,
//        @Value("${client_id}") String clientId,
//        @Value("${client_secret}") String clientSecret,
//        @Value("${redirect_uri}") String redirectUri)
//    {
//        this.userRepo = repository;
//        this.builder = builder;
//        init();
//    }


    public void init() {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            DataStoreFactory dataStore = new JPADataStoreFactory(userRepo);
            flow = builder.getCodeFlowBuilder(httpTransport, JSON_FACTORY, CLIENT_ID,
                CLIENT_SECRET, SCOPES)
                .setDataStoreFactory(dataStore)
                .setApprovalPrompt("force")
                .setAccessType("offline")
                .build();
        } catch (Exception e) {
            log.error("------ Error when configurate: {}", e);
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
            log.warn("------ Nyampe sini googleResponse");
            Credential cred = flow.createAndStoreCredential(googleResponse, userId);
            log.warn("------ Nyampe sini createAndStoreCredential");
            return true;
        } catch (Exception e) {
            log.warn("------ Error when save credential for user {} : {}", userId, e);
            return false;
        }
    }

    public GoogleCredential getCredential(String userId) {
        try {
            Credential storedCredential = flow.loadCredential(userId);
            if (storedCredential == null) {
                log.warn("------ Couldn't retrieve Google credential for user {}", userId);
                return null;
            }
            GoogleCredential credential = builder.getCredentialBuilder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                .addRefreshListener(new DataStoreCredentialRefreshListener(userId,
                    flow.getCredentialDataStore()))
                .build();

            credential.setRefreshToken(storedCredential.getRefreshToken());
            credential.setAccessToken(storedCredential.getAccessToken());
            return credential;
        } catch (Exception e) {
            log.warn("Credential: Error while refreshing or saving the token for user {} : {}",
                userId, e.getMessage());
        }

        return null;
    }

    public Calendar getCalendarService(String userId) {
        GoogleCredential credential = getCredential(userId);
        if (credential == null) {
            return null; // belom login (gada di database)
        }

        Calendar service = builder.getCalendarBuilder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();

        return service;
    }

    public Oauth2 getOauth2Service(String userId) {
        GoogleCredential credential = getCredential(userId);
        if (credential == null) {
            return null; // belom login (gada di database)
        }
        Oauth2 service = builder.getOauth2Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();

        return service;
    }

    public Userinfoplus getUserInfo(String userId) {
        Oauth2 oauth2 = getOauth2Service(userId);
        try {
            Userinfoplus userinfo = oauth2.userinfo().get().execute();
            return userinfo;
        } catch (Exception e) {
            log.warn("Userinfo: Error while execute userinfo {} : {}", userId, e.getMessage());
        }

        return null;
    }

}