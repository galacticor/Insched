package com.bot.insched.google.credentials;

import com.bot.insched.repository.DiscordUserRepository;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import java.io.IOException;

public class JPADataStoreFactory extends AbstractDataStoreFactory {

    private DiscordUserRepository userRepo;

    public JPADataStoreFactory(DiscordUserRepository repository) {
        this.userRepo = repository;
    }

    @Override
    protected JPADataStore createDataStore(String id) throws IOException {
        return new JPADataStore(this, id, userRepo);
    }
}