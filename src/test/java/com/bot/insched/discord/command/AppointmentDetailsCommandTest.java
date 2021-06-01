package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.EventService;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentDetailsCommandTest {

    @InjectMocks
    AppointmentDetailsCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    MessageSender sender;

    @Mock
    EventService eventService;

    @BeforeEach
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(command, "sender", sender);
        User user = mock(User.class);

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String mulai = LocalDateTime.now().toString();
        int duration = 30;
        int capacity = 2;
        String desc = "testing";
        Event e = new Event(mulai, duration, capacity, desc);
        e.setIdEvent(UUID.fromString("6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"));

        when(eventService.findById("6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"))
            .thenReturn(e);
        String[] args = {"6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"};
        command.execute(args, event);
    }

    @Test
    public void testExecuteSlotFull() throws Exception {
        String mulai = LocalDateTime.now().toString();
        int duration = 30;
        int capacity = 2;
        String desc = "testing";
        Event e = new Event(mulai, duration, capacity, desc);
        e.setIdEvent(UUID.fromString("6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"));
        e.setAvailable(false);

        when(eventService.findById("6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"))
            .thenReturn(e);
        String[] args = {"6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"};
        command.execute(args, event);
    }

    @Test
    public void testZeroArgs() {
        String[] args = {};
        command.execute(args, event);
    }

    @Test
    public void testExecuteNull() {
        when(eventService.findById(anyString()))
            .thenReturn(null);

        String[] args = {"6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb"};
        command.execute(args, event);
    }

    @Test
    public void testExecuteHelp() {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testInvalidUUID() {
        when(eventService.findById(anyString()))
            .thenThrow(new IllegalArgumentException());
        String[] args = {"123456-rtyui"};
        command.execute(args, event);
    }

    @Test
    public void testGetCommand() {
        String expected = "appointmentDetails";
        assertEquals(expected, command.getCommand());
    }

    @Test
    public void testGetHelp() {
        String expected = "Penggunaan: !appointmentDetails token_event\n" +
            "Contoh: !appointmentDetails 6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb";
        assertEquals(expected, command.getHelp());
    }


}
