package com.bot.insched.google;

import com.bot.insched.google.credentials.JPADataStore;
import com.bot.insched.google.credentials.JPADataStoreFactory;
import com.bot.insched.repository.DiscordUserRepository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class GoogleApiManagerTest{

	@Mock
	private DiscordUserRepository userRepo;
	@Mock
	private GoogleAuthorizationCodeFlow flow;
	@Mock
	private HttpTransport httpTransport;
	@InjectMocks
	private GoogleApiManager manager;

    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private String url = "auth url";

	@BeforeEach
	public void setUp(){
		ReflectionTestUtils.setField(manager, "CLIENT_ID", "client_id");
		ReflectionTestUtils.setField(manager, "CLIENT_SECRET", "client_secret");
		ReflectionTestUtils.setField(manager, "REDIRECT_URI", "redirect_uri");
	}

	@Test
	public void testInitFailed(){
		Builder builder = mock(Builder.class);

		ReflectionTestUtils.setField(manager, "builder", builder);

		when(builder.getCodeFlowBuilder(any(HttpTransport.class), any(JsonFactory.class), anyString(), anyString(), anyList())).thenReturn(null);
		manager.init();
	}

	@Test
	public void testInitSuccess()throws IOException {
		Builder builder = mock(Builder.class);
		GoogleAuthorizationCodeFlow.Builder flowBuilder = mock(GoogleAuthorizationCodeFlow.Builder.class);

		ReflectionTestUtils.setField(manager, "builder", builder);

		when(builder.getCodeFlowBuilder(any(HttpTransport.class), any(JsonFactory.class), anyString(), anyString(), anyList())).thenReturn(flowBuilder);
		when(flowBuilder.setDataStoreFactory(any(DataStoreFactory.class))).thenReturn(flowBuilder);
		when(flowBuilder.setApprovalPrompt(anyString())).thenReturn(flowBuilder);
		when(flowBuilder.setAccessType(anyString())).thenReturn(flowBuilder);
		manager.init();
	}

	@Test
	public void testGetAuthorizationUrl(){
		GoogleAuthorizationCodeRequestUrl codeUrl = mock(GoogleAuthorizationCodeRequestUrl.class);
		ReflectionTestUtils.setField(manager, "flow", flow);

		when(flow.newAuthorizationUrl()).thenReturn(codeUrl);
		when(codeUrl.setRedirectUri(anyString())).thenReturn(codeUrl);
		when(codeUrl.build()).thenReturn(url);

		assertNotEquals(manager.getAuthorizationUrl(),"bukan_url");
	}

	@Test
	public void testAuthTokenSuccess() throws IOException {
		String userId = "userid";
		String code = "code";
		
		Credential cred = mock(Credential.class);
		GoogleTokenResponse response = mock(GoogleTokenResponse.class);		
		GoogleAuthorizationCodeTokenRequest token = mock( GoogleAuthorizationCodeTokenRequest.class);

		ReflectionTestUtils.setField(manager, "flow", flow);

		when(flow.newTokenRequest(code)).thenReturn(token);
		when(token.setRedirectUri(anyString())).thenReturn(token);
		when(token.execute()).thenReturn(response);
		when(flow.createAndStoreCredential(response, userId)).thenReturn(cred);

		ReflectionTestUtils.setField(manager, "flow", flow);
		assertEquals(manager.authToken(userId, code), true);
	}

	@Test
	public void testAuthTokenFailed() throws IOException {
		String userId = "userid";
		String code = "code";

		assertEquals(manager.authToken(userId, code), false);
	}

	@Test
	public void testGetCredentialSuccess() throws IOException {
		String userId = "userid";
		Credential cred = mock(Credential.class);
		GoogleCredential.Builder credBuilder = mock(GoogleCredential.Builder.class);
		Builder builder = mock(Builder.class);
		JPADataStore dataStore =  mock(JPADataStore.class);
		GoogleCredential gcred = mock(GoogleCredential.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		ReflectionTestUtils.setField(manager, "httpTransport", httpTransport);
		ReflectionTestUtils.setField(manager, "builder", builder);

		when(flow.loadCredential(userId)).thenReturn(cred);
		when(flow.getCredentialDataStore()).thenReturn(dataStore);
		when(builder.getCredentialBuilder()).thenReturn(credBuilder);
		when(credBuilder.setTransport(any(HttpTransport.class))).thenReturn(credBuilder);
		when(credBuilder.setJsonFactory(any(JsonFactory.class))).thenReturn(credBuilder);
		when(credBuilder.setClientSecrets(anyString(), anyString())).thenReturn(credBuilder);
		when(credBuilder.addRefreshListener(any(DataStoreCredentialRefreshListener.class))).thenReturn(credBuilder);
		when(credBuilder.build()).thenReturn(gcred);
		when(cred.getRefreshToken()).thenReturn("refresh");
		when(cred.getAccessToken()).thenReturn("access");

		GoogleCredential gCred = manager.getCredential(userId);
		assertEquals(gCred, gcred);
	}

	@Test
	public void testGetCredentialFailedAtStoredCredential() throws IOException {
		String userId = "userid";
		Credential cred = mock(Credential.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		when(flow.loadCredential(userId)).thenReturn(null);

		assertEquals(manager.getCredential(userId),null);
	}

	@Test
	public void testGetCredentialError() throws IOException {
		String userId = "userid";
		Credential cred = mock(Credential.class);
		GoogleCredential.Builder builder = mock(GoogleCredential.Builder.class);
		JPADataStore dataStore =  mock(JPADataStore.class);
		GoogleCredential gcred = mock(GoogleCredential.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		when(flow.loadCredential(userId)).thenReturn(cred);

		assertEquals(manager.getCredential(userId), null);
		
	}

	@Test
	public void testGetCalendarServiceSuccess() throws IOException{
		String userId = "userid";
		Credential cred = mock(Credential.class);
		GoogleCredential.Builder credBuilder = mock(GoogleCredential.Builder.class);
		Builder builder = mock(Builder.class);
		JPADataStore dataStore =  mock(JPADataStore.class);
		GoogleCredential gcred = mock(GoogleCredential.class);
		JsonFactory jsonFactory = mock(JsonFactory.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		ReflectionTestUtils.setField(manager, "builder", builder);

		when(flow.loadCredential(userId)).thenReturn(cred);
		when(flow.getCredentialDataStore()).thenReturn(dataStore);
		when(builder.getCredentialBuilder()).thenReturn(credBuilder);
		when(credBuilder.setTransport(any(HttpTransport.class))).thenReturn(credBuilder);
		when(credBuilder.setJsonFactory(any(JsonFactory.class))).thenReturn(credBuilder);
		when(credBuilder.setClientSecrets(anyString(), anyString())).thenReturn(credBuilder);
		when(credBuilder.addRefreshListener(any(DataStoreCredentialRefreshListener.class))).thenReturn(credBuilder);
		when(credBuilder.build()).thenReturn(gcred);
		when(cred.getRefreshToken()).thenReturn("refresh");
		when(cred.getAccessToken()).thenReturn("access");

		when(builder.getCalendarBuilder(any(HttpTransport.class), any(JsonFactory.class), any(GoogleCredential.class)))
										.thenReturn(new Calendar.Builder(httpTransport, jsonFactory, gcred));

		assertNotEquals(manager.getCalendarService(userId), null);
	}

	@Test
	public void testGetCalendarServiceNull() {
		assertEquals(manager.getCalendarService("userId"), null);
	}

	@Test
	public void testGetOauth2ServiceSuccess() throws IOException{
		String userId = "userid";
		Credential cred = mock(Credential.class);
		GoogleCredential.Builder credBuilder = mock(GoogleCredential.Builder.class);
		Builder builder = mock(Builder.class);
		JPADataStore dataStore =  mock(JPADataStore.class);
		GoogleCredential gcred = mock(GoogleCredential.class);
		JsonFactory jsonFactory = mock(JsonFactory.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		ReflectionTestUtils.setField(manager, "builder", builder);

		when(flow.loadCredential(userId)).thenReturn(cred);
		when(flow.getCredentialDataStore()).thenReturn(dataStore);
		when(builder.getCredentialBuilder()).thenReturn(credBuilder);
		when(credBuilder.setTransport(any(HttpTransport.class))).thenReturn(credBuilder);
		when(credBuilder.setJsonFactory(any(JsonFactory.class))).thenReturn(credBuilder);
		when(credBuilder.setClientSecrets(anyString(), anyString())).thenReturn(credBuilder);
		when(credBuilder.addRefreshListener(any(DataStoreCredentialRefreshListener.class))).thenReturn(credBuilder);
		when(credBuilder.build()).thenReturn(gcred);
		when(cred.getRefreshToken()).thenReturn("refresh");
		when(cred.getAccessToken()).thenReturn("access");

		when(builder.getOauth2Builder(any(HttpTransport.class), any(JsonFactory.class), any(GoogleCredential.class)))
										.thenReturn(new Oauth2.Builder(httpTransport, jsonFactory, gcred));

		assertNotEquals(manager.getOauth2Service(userId), null);
	}

	@Test
	public void testGetOauth2ServiceNull() {
		assertEquals(manager.getOauth2Service("userId"), null);
	}

	@Test
	public void testGetUserInfoSuccess() throws IOException{
		String userId = "userid";
		Credential cred = mock(Credential.class);
		GoogleCredential.Builder credBuilder = mock(GoogleCredential.Builder.class);
		Builder builder = mock(Builder.class);
		JPADataStore dataStore =  mock(JPADataStore.class);
		GoogleCredential gcred = mock(GoogleCredential.class);
		JsonFactory jsonFactory = mock(JsonFactory.class);
		Oauth2.Builder oauth2Builder = mock(Oauth2.Builder.class);
		Oauth2 oauth2 = mock(Oauth2.class);
		Oauth2.Userinfo userinfo = mock(Oauth2.Userinfo.class);
		Oauth2.Userinfo.Get userinfoGet = mock(Oauth2.Userinfo.Get.class);
		Userinfoplus userinfoplus = mock(Userinfoplus.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		ReflectionTestUtils.setField(manager, "builder", builder);

		when(flow.loadCredential(userId)).thenReturn(cred);
		when(flow.getCredentialDataStore()).thenReturn(dataStore);
		when(builder.getCredentialBuilder()).thenReturn(credBuilder);
		when(credBuilder.setTransport(any(HttpTransport.class))).thenReturn(credBuilder);
		when(credBuilder.setJsonFactory(any(JsonFactory.class))).thenReturn(credBuilder);
		when(credBuilder.setClientSecrets(anyString(), anyString())).thenReturn(credBuilder);
		when(credBuilder.addRefreshListener(any(DataStoreCredentialRefreshListener.class))).thenReturn(credBuilder);
		when(credBuilder.build()).thenReturn(gcred);
		when(cred.getRefreshToken()).thenReturn("refresh");
		when(cred.getAccessToken()).thenReturn("access");

		when(builder.getOauth2Builder(any(HttpTransport.class), any(JsonFactory.class), any(GoogleCredential.class)))
										.thenReturn(oauth2Builder);

		when(oauth2Builder.setApplicationName(anyString())).thenReturn(oauth2Builder);
		when(oauth2Builder.build()).thenReturn(oauth2);
		when(oauth2.userinfo()).thenReturn(userinfo);
		when(userinfo.get()).thenReturn(userinfoGet);
		when(userinfoGet.execute()).thenReturn(userinfoplus);


		assertEquals(manager.getUserInfo(userId), userinfoplus);
	}

	@Test
	public void testGetUserInfoFailed() throws IOException{
		String userId = "userid";
		Credential cred = mock(Credential.class);
		GoogleCredential.Builder credBuilder = mock(GoogleCredential.Builder.class);
		Builder builder = mock(Builder.class);
		JPADataStore dataStore =  mock(JPADataStore.class);
		GoogleCredential gcred = mock(GoogleCredential.class);
		JsonFactory jsonFactory = mock(JsonFactory.class);

		ReflectionTestUtils.setField(manager, "flow", flow);
		ReflectionTestUtils.setField(manager, "builder", builder);

		when(flow.loadCredential(userId)).thenReturn(cred);
		when(flow.getCredentialDataStore()).thenReturn(dataStore);
		when(builder.getCredentialBuilder()).thenReturn(credBuilder);
		when(credBuilder.setTransport(any(HttpTransport.class))).thenReturn(credBuilder);
		when(credBuilder.setJsonFactory(any(JsonFactory.class))).thenReturn(credBuilder);
		when(credBuilder.setClientSecrets(anyString(), anyString())).thenReturn(credBuilder);
		when(credBuilder.addRefreshListener(any(DataStoreCredentialRefreshListener.class))).thenReturn(credBuilder);
		when(credBuilder.build()).thenReturn(gcred);
		when(cred.getRefreshToken()).thenReturn("refresh");
		when(cred.getAccessToken()).thenReturn("access");

		when(builder.getOauth2Builder(any(HttpTransport.class), any(JsonFactory.class), any(GoogleCredential.class)))
										.thenReturn(new Oauth2.Builder(httpTransport, jsonFactory, gcred));

		assertEquals(manager.getUserInfo(userId), null);
	}


}