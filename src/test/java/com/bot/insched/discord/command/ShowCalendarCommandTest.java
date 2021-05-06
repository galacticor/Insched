package com.bot.insched.discord.command;

import com.bot.insched.model.Appointment;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.ShowCalendarService;
import com.bot.insched.discord.command.ShowCalendarCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
public class ShowCalendarCommandTest {

    @Mock
    ShowCalendarService showCalendarService;

    @InjectMocks
    ShowCalendarCommand showCalendarCommand;

    @Mock
    PrivateMessageReceivedEvent privateMessageReceivedEvent;

    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;
    private static User jdaUser;


    @BeforeAll
    public static void init() throws Exception {
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            user.openPrivateChannel().queue(privateChannel -> {
                jdaUser = privateChannel.getUser();
                privateChannel.retrieveMessageById(messageId).queue(message1 -> {
                    message = message1;
                });
            });
        });
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);
//        init();
        lenient().when(privateMessageReceivedEvent.getAuthor()).thenReturn(jdaUser);
        lenient().when(privateMessageReceivedEvent.getMessage()).thenReturn(message);
    }

    @Test
    public void testOpening() {
        String[] args = {"Selamat Datang di fitur showCalendar"};
        showCalendarCommand.execute(args, privateMessageReceivedEvent);
    }

    @Test
    public void testGetHelp() {
        assertEquals(null, showCalendarCommand.getHelp());
    }

    @Test
    public void testGetCommand() {
        assertEquals("showCalendar", showCalendarCommand.getCommand());
    }
}
