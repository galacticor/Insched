package com.bot.insched.google;

import com.google.api.services.oauth2.Oauth2;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.calendar.Calendar;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BuilderTest{
	private Builder	builder;

	@BeforeEach
	public void setUp(){
		builder = new Builder();
	}

	@Test
	public void testGetCredentialBuilder() {
		assertNotEquals(builder.getCredentialBuilder(),null);
	}

	@Test
	public void testGetCalendarBuilder() {
		HttpTransport transport = mock(HttpTransport.class);
		JsonFactory jsonFactory = mock(JsonFactory.class);
		GoogleCredential cred = mock(GoogleCredential.class);

		Calendar.Builder cal = builder.getCalendarBuilder(transport, jsonFactory, cred);

		assertEquals(cal.getTransport(), transport);
		assertEquals(cal.getJsonFactory(), jsonFactory);
	}

	@Test
	public void testGetOauth2Builder() {
		HttpTransport transport = mock(HttpTransport.class);
		JsonFactory jsonFactory = mock(JsonFactory.class);
		GoogleCredential cred = mock(GoogleCredential.class);

		Oauth2.Builder oauth2 = builder.getOauth2Builder(transport, jsonFactory, cred);

		assertEquals(oauth2.getTransport(), transport);
		assertEquals(oauth2.getJsonFactory(), jsonFactory);
	}
}