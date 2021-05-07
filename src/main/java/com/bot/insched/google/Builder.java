package com.bot.insched.google;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.oauth2.Oauth2;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public class Builder {

    public GoogleCredential.Builder getCredentialBuilder() {
        return new GoogleCredential.Builder();
    }

    public Calendar.Builder getCalendarBuilder(HttpTransport httpTransport,
                                               JsonFactory jsonFactory,
                                               GoogleCredential credential) {
        return new Calendar.Builder(httpTransport, jsonFactory, credential);
    }

    public Oauth2.Builder getOauth2Builder(HttpTransport httpTransport, JsonFactory jsonFactory,
                                           GoogleCredential credential) {
        return new Oauth2.Builder(httpTransport, jsonFactory, credential);
    }

    public GoogleAuthorizationCodeFlow.Builder getCodeFlowBuilder(
        HttpTransport httpTransport,
        JsonFactory jsonFactory,
        String clientId,
        String clientSecret,
        List<String> scopes) {
        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientId,
            clientSecret, scopes);
    }
}