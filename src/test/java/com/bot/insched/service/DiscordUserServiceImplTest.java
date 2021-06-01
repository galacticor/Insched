package com.bot.insched.service;

import com.bot.insched.model.DiscordUser;
import com.bot.insched.repository.DiscordUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiscordUserServiceImplTest {

    @InjectMocks
    DiscordUserServiceImpl service;

    @Mock
    DiscordUserRepository repository;

    DiscordUser user;
    String idDiscord = "123";

    @BeforeEach
    public void setUp() {
        user = new DiscordUser();
        user.setIdDiscord("123");
    }

    @Test
    public void testFindByUserId() {
        when(repository.findByIdDiscord(idDiscord)).thenReturn(user);
        service.findByUserId(idDiscord);
    }

    @Test
    public void testSave() {
        // doNothing().when(repository).save(user);
        service.save(user);
    }

    @Test
    public void testLogout() {
        when(repository.findByIdDiscord(idDiscord)).thenReturn(user);
        // doNothing().when(repository).save(user);
        service.logout(idDiscord);
    }

    @Test
    public void testLogoutUserNull() {
        when(repository.findByIdDiscord(anyString()))
            .thenReturn(null);
        service.logout(idDiscord);
    }

}
