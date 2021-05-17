package com.bot.insched.google.credentials;

import com.bot.insched.repository.DiscordUserRepository;
import com.bot.insched.model.DiscordUser;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;

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
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
public class JPADataStoreFactoryTest {
	@Mock
	private DiscordUserRepository repository;

	@InjectMocks
    private JPADataStoreFactory dataStoreFactory;

    private JPADataStore dataStore;

    private DiscordUser user;
    private StoredCredential storedCredential;
    private String accessToken = "dummy_access_token";
    private String refreshToken = "dummy_refresh_token";
    private String id = "123";


    @BeforeEach
    public void setUp() throws IOException {
    	dataStore = dataStoreFactory.createDataStore(id);
    	storedCredential = new StoredCredential();
        storedCredential.setAccessToken(accessToken);
        storedCredential.setRefreshToken(refreshToken);
        user = new DiscordUser(id, storedCredential);
    }

    @Test
    public void testGeneral() throws IOException{
    	ReflectionTestUtils.setField(dataStore, "jpaDataStoreFactory", dataStoreFactory);
    	assertEquals(dataStore.getDataStoreFactory(), dataStoreFactory);

    	when(repository.findByIdDiscord(id)).thenReturn(user);
    	assertEquals(dataStore.containsKey(id), true);
    	when(repository.findByIdDiscord(id)).thenReturn(null);
    	assertEquals(dataStore.containsKey(id), false);

    	when(repository.findByAccessToken(accessToken)).thenReturn(user);
    	assertEquals(dataStore.containsValue(storedCredential), true);
    	when(repository.findByAccessToken(accessToken)).thenReturn(null);
    	assertEquals(dataStore.containsValue(storedCredential), false);

    	Set<String> keys = new HashSet<>();
    	when(repository.findAllKeys()).thenReturn(keys);
    	assertEquals(dataStore.keySet(), keys);

    	when(repository.count()).thenReturn((long) 0);
    	assertEquals(dataStore.isEmpty(), true);
    	when(repository.count()).thenReturn((long) 100);
    	assertEquals(dataStore.isEmpty(), false);

    	doNothing().when(repository).deleteAll();
    	assertEquals(dataStore.clear(), dataStore);

    	doNothing().when(repository).deleteById(id);
    	assertEquals(dataStore.delete(id), dataStore);

    	assertEquals(dataStore.values(), null);
    }

    @Test
    public void testGetCredentialSuccess() throws IOException{
    	when(repository.findByIdDiscord(id)).thenReturn(user);

    	StoredCredential credential = dataStore.get(id);
    	assertEquals(credential.getAccessToken(), accessToken);
    	assertEquals(credential.getRefreshToken(), refreshToken);
    }

    @Test
    public void testGetCredentialFailed() throws IOException{
    	when(repository.findByIdDiscord(id)).thenReturn(null);
    	assertEquals(dataStore.get(id), null);
    }

    @Test
    public void testSetCredentialSuccess()throws IOException {
    	when(repository.findByIdDiscord(id)).thenReturn(user);
    	assertEquals(dataStore.set(id, storedCredential), dataStore);
    }

    @Test
    public void testSetCredentialFailed()throws IOException {
    	when(repository.findByIdDiscord(id)).thenReturn(null);
    	assertEquals(dataStore.set(id, storedCredential), dataStore);
    }

}