package com.bot.insched.discord.command;

import com.bot.insched.model.Appointment;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MyAppointmentListCommandTest {

    @InjectMocks
    MyAppointmentListCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    DiscordUserService discordUserService;

    @Mock
    AppointmentService appointmentService;

    // Basic test setup
    private static JDA jda;
    private static String userId = "461191404341821455";
    private static Message message;
    private static User jdaUser;

    @BeforeAll
    public static void init() throws Exception {
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            jdaUser = user;
        });
        message = new MessageBuilder().append("dummy").build();
    }

    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }


    @Test
    public void testExecuteHelp() throws Exception {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testExecuteSuccess() {
        String[] args = {"2021-05-08"};
        List<Event> eventList = new ArrayList<>();

        String start = "2021-05-08T17:00:00";
        int duration = 30;
        int capacity = 2;
        String desc = "testing";

        Event e = new Event(start, duration, capacity, desc);
        eventList.add(e);

        when(appointmentService.getAllAppointment(userId)).thenReturn(eventList);

        command.execute(args, event);
    }

    @Test
    public void testGetCommand() {
        String res = command.getCommand();
        String expected = "myAppointmentList";
        assertEquals(res, expected);

    }


}
