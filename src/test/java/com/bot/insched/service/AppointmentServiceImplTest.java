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

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    DiscordUserService discordUserService;

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
        app.setIdAppointment(UUID.fromString("70bab1b3-7e8e-4b3e-84b5-e95901e1925d"));
        user.setAppointment(app);

        String response = appointmentService.getUserToken(user);
        assertEquals(response, "70bab1b3-7e8e-4b3e-84b5-e95901e1925d");
    }

//    @Test
//    public void testGetUserTokenNull() {
//        String response = appointmentService.getUserToken(user);
//        assertEquals(response, anyString());
//    }

    @Test
    public void testCreateSlotNoUser() {
        lenient().when(discordUserService.findByUserId(any())).thenReturn(null);
        assertThrows(Exception.class , () -> {
            appointmentService.createSlot("aa","2021-05-03T15:30:00",1,1,"123");
        });
    }

    @Test
    public void testCreateAlreadyExists() {
        lenient().when(discordUserService.findByUserId(anyString())).thenReturn(user);


        List<Event> userAppointment = new ArrayList<>();
        Event event = new Event("2021-03-05T15:30", 30, 2, "dummy");
        userAppointment.add(event);
        user.setAppointment(appointment);
        user.getAppointment().setListEvent(userAppointment);

        assertThrows(Exception.class, () -> {
            appointmentService.createSlot("dummy", "2021-03-05T15:30", 30,  2, "123");
        });
    }

    @Test
    public void testCreateSlotSuccess() throws Exception{
        lenient().when(discordUserService.findByUserId(anyString())).thenReturn(user);
        List<Event> userAppointment = new ArrayList<>();
        user.setAppointment(appointment);
        user.getAppointment().setListEvent(userAppointment);

        when(appointmentRepository.findAppointmentByOwner(any()))
                .thenReturn(appointment);

        String res = appointmentService.createSlot("aa","2021-05-03T15:30:00",1,1,"123");
        assertEquals(res, "Slot berhasil dibuat!");
    }

    @Test
    public void testGetAllAppointment() {
        when(discordUserService.findByUserId(any())).thenReturn(user);
        when(appointmentRepository.findAppointmentByOwner(any())).thenReturn(appointment);
        appointment.setListEvent(new ArrayList<>());
        List<Event> expected = appointment.getListEvent();
        assertEquals(expected, appointmentService.getAllAppointment("123"));

    }



}
