package com.bot.insched.discord.command;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.EventService;
import com.bot.insched.service.EventServiceImpl;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.services.calendar.Calendar;
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class CreateEventCommandTest {
    @Mock
    private EventService eventService;

    @Mock
    private PrivateMessageReceivedEvent event;

    @InjectMocks
    private CreateEventCommand command;


    // Basic test setup
    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;

    private DiscordUser user;
    private StoredCredential storedCredential;
    private static User jdaUser;

    private Calendar calendar;


    @BeforeAll
    public static void init() throws Exception {
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            jdaUser = user;
        });
        message = new MessageBuilder().append("dummy message").build();
    }


    // Thread.sleep to delay and prevent error
    @BeforeEach
    public void setUp() throws Exception {
        Thread.sleep(1000);
        storedCredential = new StoredCredential();
        storedCredential.setAccessToken("dummy");
        storedCredential.setRefreshToken("dummy");
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
        lenient().when(event.getMessage()).thenReturn(message);
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

    @Test
    public void testCorrectArgument() throws Exception {
        String[] args = {"!createEvent","KUIS","2021-05-20", "15:30", "2021-05-21" ,"15:30","Kuliah"};
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.createEventService(any(), any()))
                .thenReturn("Event Berhasil dibuat");
        command.execute(args, event);
    }

    @Test
    public void testErrorArgument() throws Exception {
        String[] args = {"!createEvent","a","a","a","a"};
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.createEventService(any(), any()))
                .thenReturn("error");
        command.execute(args, event);
    }


    @Test
    public void testFalseArgument() {
        String[] args = {"dummy", "dummy", "dummy"};
        command.execute(args, event);
    }

    @Test
    public void testHelpArgument() {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "!createEvent <summary> <token_event> <tanggal_mulai> <jam_mulai> " +
                "<tanggal_selesai> <jam_selesai> <deskripsi_event> \n" +
                "Contoh: !createEvent KUIS 2021-05-20 15:30 2021-05-21 15:30 Kuliah";
        assertEquals(command.getHelp(), expected);
    }
}
