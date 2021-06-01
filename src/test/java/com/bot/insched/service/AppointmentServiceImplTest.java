package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import com.bot.insched.repository.EventRepository;
import com.google.api.client.auth.oauth2.StoredCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    DiscordUserService discordUserService;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    AppointmentServiceImpl appointmentService;

    @Mock
    EventService eventService;

    @Mock
    DiscordUser dummyUser;

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

    @Test
    public void testGetUserTokenNull() {
        dummyUser.setAppointment(null);
        ReflectionTestUtils.setField(appointmentService, "discordUserService",
            discordUserService);
        ReflectionTestUtils.setField(appointmentService, "appointmentRepository",
            appointmentRepository);
        lenient().when(discordUserService.save(any(DiscordUser.class))).thenReturn(dummyUser);

        Appointment app = new Appointment();
        UUID dummyUUID = UUID.randomUUID();
        app.setIdAppointment(dummyUUID);

        when(appointmentService.save(any(Appointment.class)))
            .thenReturn(app);

//        String response = appointmentService.getUserToken(dummyUser);
//        assertEquals(response, dummyUser.getAppointment().getIdAppointment().toString());
        assertThrows(NullPointerException.class, () -> {
            appointmentService.getUserToken(dummyUser);
        });
    }

    @Test
    public void testCreateSlotNoUser() {
        lenient().when(discordUserService.findByUserId(any())).thenReturn(null);
        assertThrows(Exception.class, () -> {
            appointmentService.createSlot("aa", "2021-05-03T15:30:00", 1, 1, "123");
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

        assertThrows(SlotUnavailableException.class, () -> {
            appointmentService.createSlot("dummy", "2021-03-05T15:30", 30, 2, "123");
        });
    }

    @Test
    public void testCreateSlotSuccess() throws Exception {
        lenient().when(discordUserService.findByUserId(anyString())).thenReturn(user);
        List<Event> userAppointment = new ArrayList<>();
        user.setAppointment(appointment);
        user.getAppointment().setListEvent(userAppointment);

        when(appointmentRepository.findAppointmentByOwner(any())).thenReturn(appointment);
        ReflectionTestUtils.setField(appointmentService, "eventService", eventService);

        String res = appointmentService.createSlot("aa", "2021-05-03T15:30:00", 1, 1, "123");
        assertEquals(res, "Slot berhasil dibuat!");
    }

    @Test
    public void testBranchingInCreateSlot() throws Exception {
        lenient().when(discordUserService.findByUserId(anyString())).thenReturn(user);

        List<Event> userAppointment = new ArrayList<>();
        Event event = new Event("2021-02-05T15:30", 30, 2, "dummy");
        Event event2 = new Event("2021-03-05T15:30", 30, 2, "dummy");
        userAppointment.add(event);
        userAppointment.add(event2);
        user.setAppointment(appointment);
        user.getAppointment().setListEvent(userAppointment);

        assertThrows(SlotUnavailableException.class, () -> {
            appointmentService.createSlot("dummy", "2021-03-05T15:30", 30, 2, "123");
        });
    }

    @Test
    public void testGetAllAppointment() throws Exception {
        when(discordUserService.findByUserId(any())).thenReturn(user);
        when(appointmentRepository.findAppointmentByOwner(any())).thenReturn(appointment);
        appointment.setListEvent(new ArrayList<>());
        List<Event> expected = appointment.getListEvent();
        assertEquals(expected, appointmentService.getAllAppointment("123"));
    }

    @Test
    public void testGetAllAppointmentNotLoggedIn() throws Exception {
        when(discordUserService.findByUserId(any())).thenReturn(null);

        assertThrows(NotLoggedInException.class, () -> {
            appointmentService.getAllAppointment("123");
        });
    }

    @Test
    public void testEditSlotUserNotExists() throws Exception {
        when(discordUserService.findByUserId(anyString())).thenReturn(null);
        String token = "dummy_token";
        String jamBaru = "15:30";
        int durasiBaru = 30;
        String judulBaru = "dummy_judul";
        String idDiscord = "123";

        assertThrows(NotLoggedInException.class, () -> {
            appointmentService.editSlot(token, jamBaru, durasiBaru, judulBaru, idDiscord);
        });
    }

    @Test
    public void testEventNotExists() throws Exception {
        DiscordUser user = new DiscordUser();
        when(discordUserService.findByUserId(anyString()))
            .thenReturn(user);

        when(eventRepository.findByIdEvent(any(UUID.class)))
            .thenReturn(null);

        String token = UUID.randomUUID().toString();
        String jamBaru = "15:30";
        int durasiBaru = 30;
        String judulBaru = "dummy_judul";
        String idDiscord = "123";

        String res = appointmentService.editSlot(token, jamBaru, durasiBaru, judulBaru, idDiscord);
        assertEquals(res, "Tidak ada slot dengan kode tersebut!");

    }

    @Test
    public void testEventExists() throws Exception {
        DiscordUser user = new DiscordUser();
        Event event = new Event();
        event.setListAttendee(new ArrayList<>());
        event.setStartTime(LocalDateTime.parse("2021-07-09T15:00:00"));
        when(discordUserService.findByUserId(anyString()))
            .thenReturn(user);

        when(eventRepository.findByIdEvent(any(UUID.class)))
            .thenReturn(event);

        String token = UUID.randomUUID().toString();
        String jamBaru = "15:30";
        int durasiBaru = 30;
        String judulBaru = "dummy_judul";
        String idDiscord = "123";

        String res = appointmentService.editSlot(token, jamBaru, durasiBaru, judulBaru, idDiscord);
        assertEquals(res, "Appointment berhasil di-update!");
    }

    @Test
    public void testEventAlreadyAttended() throws Exception {
        DiscordUser user = new DiscordUser();
        Event event = new Event();
        List<DiscordUser> list = new ArrayList<>();
        list.add(user);
        event.setListAttendee(list);
        event.setStartTime(LocalDateTime.parse("2021-07-09T15:00:00"));

        when(discordUserService.findByUserId(anyString()))
            .thenReturn(user);

        when(eventRepository.findByIdEvent(any(UUID.class)))
            .thenReturn(event);

        String token = UUID.randomUUID().toString();
        String jamBaru = "15:30";
        int durasiBaru = 30;
        String judulBaru = "dummy_judul";
        String idDiscord = "123";

        String res = appointmentService.editSlot(token, jamBaru, durasiBaru, judulBaru, idDiscord);
        assertEquals(res, "Jangan melanggar ketentuan yang sudah diberikan!");
    }


    @Test
    public void testDeleteSlotUserNotExists() {
        when(discordUserService.findByUserId(anyString()))
            .thenReturn(null);

        assertThrows(NotLoggedInException.class, () -> {
            appointmentService.deleteSlot("dummy", "dummy");
        });
    }

    @Test
    public void testDeleteSlotEventNull() {
        DiscordUser user = new DiscordUser();
        when(discordUserService.findByUserId(anyString())).thenReturn(user);

        when(eventRepository.findByIdEvent(any(UUID.class))).thenReturn(null);

        assertThrows(SlotUnavailableException.class, () -> {
            appointmentService.deleteSlot(UUID.randomUUID().toString(), "dummy_id");
        });
    }

    @Test
    public void testDeleteSlotHaveBeenBooked() throws Exception {
        DiscordUser user = new DiscordUser();
        Event event = new Event();
        ArrayList<DiscordUser> list = new ArrayList<>();
        list.add(user);
        event.setListAttendee(list);

        when(discordUserService.findByUserId(anyString())).thenReturn(user);
        when(eventRepository.findByIdEvent(any(UUID.class))).thenReturn(event);

        String response =
            appointmentService.deleteSlot(UUID.randomUUID().toString(), "dummy_id");

        assertEquals(response, "Slot sudah dibooking!");
    }

    @Test
    public void testDeleteSlotSuccess() throws Exception {
        DiscordUser user = new DiscordUser();
        Event event = new Event();
        event.setIdEvent(UUID.randomUUID());
        ArrayList<DiscordUser> list = new ArrayList<>();
        event.setListAttendee(list);

        ReflectionTestUtils.setField(appointmentService, "eventRepository", eventRepository);

        when(discordUserService.findByUserId(anyString())).thenReturn(user);
        when(eventRepository.findByIdEvent(any(UUID.class))).thenReturn(event);
        doNothing().when(eventService).deleteEventFromRepo(any(UUID.class));

        String response =
            appointmentService.deleteSlot(event.getIdEvent().toString(),"dummy_id");

        assertEquals(response, "Slot berhasil dihapus!");
    }

}
