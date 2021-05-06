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
        appointment = new Appointment("deskripsi", LocalDate.now(), LocalDate.now());
    }


    // Create Appointment Block Test
    @Test
    public void testCreateAppointmentSuccess() throws Exception{
        when(discordUserRepository.findByIdDiscord(any(String.class))).thenReturn(user);
        String end_date = start_date.toString();
        String res = appointmentService.createAppointment(desc, start_date.toString(), end_date, "123456");
        assertEquals(res, "Appointment berhasil dibuat!");
    }

    @Test
    public void testCreateAppointmentNotLoggedIn() throws Exception{
        when(discordUserRepository.findByIdDiscord(any(String.class))).thenReturn(null);
        String startDate = start_date.toString();
        String endDate = startDate;
        String res = appointmentService.createAppointment(desc, startDate, endDate, "123456");
        assertEquals(res, "Silahkan login terlebih dahulu menggunakan !login");
    }

    @Test
    public void testCreateAppointmentDateError() throws Exception {
        when(discordUserRepository.findByIdDiscord(any(String.class))).thenReturn(user);
        String end_date = LocalDate.now().minusDays(1).toString();
        String res = appointmentService.createAppointment(desc, start_date.toString(), end_date, "123456");
        assertEquals(res, "Appointment yang dibuat hanya dapat dimulai dari hari ini dan selesai minimal hari ini!");
    }


    // Get user's Appointment test
    @Test
    public void testGetAllUserAppointment() {
        List<Appointment> appList = new ArrayList<>();
        appList.add(appointment);
        when(appointmentRepository.findAllByOwner(any())).thenReturn(appList);
        assertEquals(appointmentService.getAllUserAppointment("123456"), appList);
    }

    @Test
    public void testFindAppointmentById() {
        when(appointmentRepository.findByIdAppointment(any()))
                .thenReturn(appointment);
        assertEquals(
                appointmentService.findAppointmentById("0a22933c-eece-4c37-bccd-9e47d990ead1"),
                appointment);
    }

    @Test
    public void testCreateSlot() {
        appointment.setListEvent(new ArrayList<Event>());
        when(appointmentService.save(any())).thenReturn(appointment);
        String res = appointmentService.createSlot("2024-03-24T15:30:00",30, 2, appointment);
        assertEquals(res, "Slot berhasil dibuat!");
    }

}
