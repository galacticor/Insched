package com.bot.insched.google.credentials;

import com.bot.insched.model.DiscordUser;
import com.bot.insched.repository.DiscordUserRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;


public class JPADataStore extends AbstractDataStore<StoredCredential> {
    private DiscordUserRepository repository;
    private JPADataStoreFactory jpaDataStoreFactory;

    /**
     * @param dataStoreFactory data store factory
     * @param id               data store ID
     */
    protected JPADataStore(JPADataStoreFactory dataStoreFactory, String id,
                           DiscordUserRepository repository) {
        super(dataStoreFactory, id);
        this.repository = repository;
    }

    @Override
    public JPADataStoreFactory getDataStoreFactory() {
        return jpaDataStoreFactory;
    }

    @Override
    public int size() throws IOException {
        return (int) repository.count();
    }

    @Override
    public boolean isEmpty() throws IOException {
        return size() == 0;
    }

    @Override
    public boolean containsKey(String idDiscord) throws IOException {
        return repository.findByIdDiscord(idDiscord) != null;
        // return false;
    }

    @Override
    public boolean containsValue(StoredCredential value) throws IOException {
        return repository.findByAccessToken(value.getAccessToken()) != null;
        // return false;
    }

    @Override
    public Set<String> keySet() throws IOException {
        return repository.findAllKeys();
    }

    @Override
    public Collection<StoredCredential> values() throws IOException {
        // return repository.findAll().stream().map(c -> {
        //     StoredCredential credential = new StoredCredential();
        //     credential.setAccessToken(c.getAccessToken());
        //     credential.setRefreshToken(c.getRefreshToken());
        //     credential.setExpirationTimeMilliseconds(c.getExpirationTime());
        //     return credential;
        // }).collect(Collectors.toList());
        return null;
    }

    @Override
    public StoredCredential get(String id) throws IOException {
        DiscordUser user = repository.findByIdDiscord(id);
        if (user == null) {
            return null;
        }
        StoredCredential credential = new StoredCredential();
        credential.setAccessToken(user.getAccessToken());
        credential.setRefreshToken(user.getRefreshToken());
        return credential;
    }

    @Override
    public DataStore<StoredCredential> set(String id, StoredCredential cred) throws IOException {
        DiscordUser user = repository.findByIdDiscord(id);
        if (user == null) {
            user = new DiscordUser(id, cred);
            repository.save(user);
        } else {
            user.apply(cred);
            repository.save(user);
        }
        return this;
    }

    @Override
    public DataStore<StoredCredential> clear() throws IOException {
        repository.deleteAll();
        return this;
    }

    @Override
    public DataStore<StoredCredential> delete(String id) throws IOException {
        repository.deleteById(id);
        return this;
    }
}