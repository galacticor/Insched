package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.service.BookingAppointmentService;
import com.bot.insched.service.EventService;
import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookAppointmentCommandTest {

    @InjectMocks
    BookAppointmentCommand command;

    @Mock
    BookingAppointmentService service;

    @Mock
    EventService eventService;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    private MessageSender sender;

    @Mock
    GoogleService googleService;

    private final String dummyId = "0";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(command, "sender", sender);
        User user = mock(User.class);
        DiscordUser dcUser = new DiscordUser();
        dcUser.setIdDiscord("123");

        Appointment app = new Appointment();
        app.setIdAppointment(UUID.randomUUID());
        app.setOwner(dcUser);

        Event e = new Event();
        e.setIdEvent(UUID.randomUUID());
        e.setStartTime(LocalDateTime.now());
        e.setEndTime(LocalDateTime.now());
        e.setAppointment(app);

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn(dummyId);
        lenient().when(eventService.findById(anyString()))
            .thenReturn(e);
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String res = "Booking slot event telah dibuat!";
        String email = "testing@gmail.com";
        String[] args = {dummyToken};
        lenient().when(googleService.getUserInfo(anyString())).thenReturn(email);
        lenient().when(service.createBooking(dummyId, dummyToken, email)).thenReturn(res);
        command.execute(args, event);
    }

    @Test
    public void testExecuteSuccess2() throws Exception {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String res = "Booking slot event telah dibuat!";
        String email = "testing@gmail.com";
        String[] args = {dummyToken, email};
        lenient().when(service.createBooking(dummyId, dummyToken, email)).thenReturn(res);
        command.execute(args, event);
    }

    @Test
    public void testWrongArguments() {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String extra = "extra";
        String[] args = {dummyToken, extra};

        String res =
                "Masukkan argumen yang sesuai!\n"
                        + "Penggunaan: !bookAppointment <token_event>\n"
                        + "Help: !bookAppointment help";
        lenient().doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testGeneralException() throws Exception {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String[] args = {dummyToken};
        String dummyemail = "dummy@gmail.com";

        lenient().when(service.createBooking(dummyId, dummyToken, dummyemail))
                .thenThrow(new Exception("dummy exception"));
        command.execute(args, event);
    }

    @Test
    public void testIndexOutOfBounds() {
        String[] args = {};
        command.execute(args, event);

    }

    @Test
    public void testHelpCommand() {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        assertEquals(res,
            "Digunakan untuk membuat booking pada slot event dalam sebuah appointment.\n"
                + "Penggunaan: !bookAppointment <token_event> <email_kamu>\n"
                + "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2 joe@gmail.com\n"
                + "Atau jika kamu sudah login, maka\n"
                + "Penggunaan: !bookAppointment <token_event>\n"
                + "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2");
    }

    @Test
    public void testGetCommand() {
        assertEquals(command.getCommand(), "bookAppointment");
    }

    @Test
    public void testCreationHandlerZeroArgs() {
        String[] args = {};
        assertThrows(IndexOutOfBoundsException.class, () -> {
            command.creationHandler(args, event);
        });
    }

    @Test
    public void testCreationHandlerOneArgs() throws Exception {
        String[] args = {"dummyToken"};
        when(googleService.getUserInfo(anyString()))
            .thenReturn("testing@gmail.com");
        lenient().when(service.createBooking(anyString(), anyString(), anyString()))
            .thenReturn("Booking Slot event telah dibuat!");

        command.creationHandler(args, event);
    }


}
