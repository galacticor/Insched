package com.bot.insched.discord.command;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.AppointmentService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MyAppointmentCommandTest {

    @InjectMocks
    MyAppointmentCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    AppointmentService appointmentService;

    @Mock
    MessageSender sender;

    @BeforeEach
    public void setUp() throws Exception {
        User user = mock(User.class);
        ReflectionTestUtils.setField(command, "sender", sender);
        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String[] args = {};
        Event e = new Event();
        e.setStartTime(LocalDateTime.parse("2022-09-09T15:00:00"));
        List<Event> eventList = new ArrayList<>();
        eventList.add(e);
        when(appointmentService.getAllAppointment(anyString()))
            .thenReturn(eventList);
        command.execute(args, event);
    }

    @Test
    public void testThrowException() throws Exception {
        when(appointmentService.getAllAppointment(anyString()))
            .thenThrow(new NotLoggedInException());
        String[] args = {};
        command.execute(args, event);
    }

    @Test
    public void testGetCommandandGetHelp() {
        String expected = "myAppointment";
        assertEquals(expected, command.getCommand());
        assertEquals(expected, command.getHelp());
    }

}
