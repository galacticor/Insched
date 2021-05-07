package com.bot.insched.service;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.DiscordUserRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DiscordUserRepository discordUserRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    AppointmentServiceImpl appointmentService;


    private DiscordUser user;
    private StoredCredential storedCredential;
    private Appointment appointment;

    private String accessToken = "dummy_access_token";
    private String refreshToken = "dummy_refresh_token";
    private String desc = "ini_deskripsi";
    private LocalDate start_date = LocalDate.now();


    @BeforeEach
    public void setUp() {
        storedCredential = new StoredCredential();
        storedCredential.setAccessToken(accessToken);
        storedCredential.setRefreshToken(refreshToken);
        user = new DiscordUser("123456", storedCredential);
        appointment = new Appointment();
    }


    @Test
    public void testGetUserToken() {
        Appointment app = new Appointment();
        app.setIdAppointment(UUID.fromString("123"));
        user.setAppointment(app);

        String response = appointmentService.getUserToken(user);
        assertEquals(response, "123");
    }




}
