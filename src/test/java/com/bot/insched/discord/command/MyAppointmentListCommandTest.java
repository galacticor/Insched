package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.annotation.Order;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Order
public class MyAppointmentListCommandTest {

    @InjectMocks
    MyAppointmentListCommand command;

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
    @Order(1)
    public void testExecuteHelp() throws Exception {
        String[] args = { "help" };
        String res = "!myAppointmentList tanggal\n" +
            "Contoh: !myAppointmentList 2021-05-03";
        lenient().doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    @Order(2)
    public void testExecuteSuccess() throws Exception {
        String[] args = { "2021-05-08" };
        List<Event> eventList = new ArrayList<>();

        String start = "2021-05-08T17:00:00";
        int duration = 30;
        int capacity = 2;
        String desc = "testing";
        String userId = "461191404341821455";

        Event e = new Event(start, duration, capacity, desc);
        eventList.add(e);

        when(appointmentService.getAllAppointment(userId)).thenReturn(eventList);
        InschedEmbed embed = new InschedEmbed();
        embed.setDescription("dummy embed");

        lenient().doNothing().when(sender).sendPrivateMessage(embed.build(), event);
        command.execute(args, event);
    }

    @Test
    @Order(3)
    public void testGetCommand() {
        String res = command.getCommand();
        String expected = "myAppointmentList";
        assertEquals(res, expected);

    }

}
