package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.EventService;
import com.bot.insched.service.EventServiceImpl;
import com.bot.insched.service.GoogleService;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.EventDateTime;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CreateEventCommandTest {
    @Mock
    private EventService eventService;

    @Mock
    private PrivateMessageReceivedEvent event;

    @Mock
    private MessageSender sender;

    @Mock
    GoogleService googleService;

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
        User user = mock(User.class);
        ReflectionTestUtils.setField(command, "sender", sender);
        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }


    @Test
    public void testCorrectArgument() throws Exception {
        InschedEmbed embed = mock(InschedEmbed.class);
        PrivateChannel channel = mock(PrivateChannel.class);
        MessageAction action = mock(MessageAction.class);
        User user = mock(User.class);
        String ret = "Event Berhasil dibuat";
        embed.setTitle("Create Event");
        embed.setDescription(ret);
        String[] args = {"idEvent","KUIS","2021-05-20", "15:30", "2021-05-21" ,"15:30","Kuliah"};
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.createEventService(any(), any()))
                .thenReturn(ret);
        lenient().when(event.getChannel()).thenReturn(channel);
        lenient().when(channel.sendMessage(anyString())).thenReturn(action);
        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
        lenient().doNothing().when(sender).sendPrivateMessage(ret, event);
        command.execute(args, event);
    }

    @Test
    public void testCorrectArgumentDesription() throws Exception {
        InschedEmbed embed = mock(InschedEmbed.class);
        PrivateChannel channel = mock(PrivateChannel.class);
        MessageAction action = mock(MessageAction.class);
        User user = mock(User.class);
        String ret = "Event Berhasil dibuat";
        embed.setTitle("Create Event");
        embed.setDescription(ret);
        String[] args = {"idEvent","KUIS","2021-05-20", "15:30", "2021-05-21" ,"15:30","Kuliah","adpro"};
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.createEventService(any(), any()))
                .thenReturn(ret);
        lenient().when(event.getChannel()).thenReturn(channel);
        lenient().when(channel.sendMessage(anyString())).thenReturn(action);
        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
        lenient().doNothing().when(sender).sendPrivateMessage(ret, event);
        command.execute(args, event);
    }

    @Test
    public void testHelpArgument() {
        PrivateChannel channel = mock(PrivateChannel.class);
        MessageAction action = mock(MessageAction.class);
        String[] args = {"help"};
        lenient().when(event.getChannel()).thenReturn(channel);
        lenient().when(channel.sendMessage(anyString())).thenReturn(action);
        command.execute(args, event);
    }

    @Test
    public void testErrorArgument() throws Exception {
        String[] args = {"a","a","a","a"};
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
    public void testGetHelp() {
        String expected = "!createEvent <id_event> <judul> <tanggal_mulai> <jam_mulai> "
                + "<tanggal_selesai> <jam_selesai> <deskripsi_event> <deskripsi_event> ...\n"
                + "Contoh: !createEvent 0123klm4o5678abdefhij9prstuv "
                + "belajar 2000-04-22 15:30 2000-05-23 15:30 Kuliah adpro \n"
                + "note: pastikan id event lebih dari 10 karakter yang terdiri dari angka dan huruf";
        assertEquals(command.getHelp(), expected);
    }

    @Test
    public void testGetCommand() {
        assertEquals(command.getCommand(), "createEvent");
    }
}
