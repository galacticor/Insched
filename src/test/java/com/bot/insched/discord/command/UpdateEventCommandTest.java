package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.EventService;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.services.calendar.Calendar;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateEventCommandTest {
    @Mock
    private EventService eventService;

    @Mock
    private PrivateMessageReceivedEvent event;

    @InjectMocks
    private UpdateEventCommand command;

    @Mock
    private MessageSender sender;



    // Basic test setup
    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;


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
        String[] args = {"fjbqeoaufbqeo", "summary", "Kuliah"};
        User user = mock(User.class);
        InschedEmbed embed = new InschedEmbed();
        String ret = "Event Berhasil diperbaharui";
        embed.setTitle("Update Event");
        embed.setDescription("Event Berhasil diperbaharui");
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.updateEventService(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(ret);
        lenient().when(user.getId()).thenReturn("123");
        lenient().doNothing().when(sender).sendPrivateMessage(embed.build(), event);
        command.execute(args, event);
    }

    @Test
    public void testCorrectArgumentDescription() throws Exception {
        String[] args = {"fjbqeoaufbqeo", "deskripsi", "Kuliah","adpro","ada"};
        User user = mock(User.class);
        InschedEmbed embed = new InschedEmbed();
        String ret = "Event Berhasil diperbaharui";
        embed.setTitle("Update Event");
        embed.setDescription("Event Berhasil diperbaharui");
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.updateEventService(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(ret);
        lenient().when(user.getId()).thenReturn("123");
        lenient().doNothing().when(sender).sendPrivateMessage(embed.build(), event);
        command.execute(args, event);
    }

    @Test
    public void testErrorArgument() throws Exception {
        String[] args = {"selesai"};
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.updateEventService(anyString(), anyString(), anyString(), anyString()))
                .thenReturn("error");
        command.execute(args, event);
    }


    @Test
    public void testFalseArgument() {
        String[] args = {"dummy", "dummy"};
        command.execute(args, event);
    }

    @Test
    public void testHelpArgument() {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "!updateEvent <eventID> <jenis> <dataBaru> \n"
                + "Contoh: !updateEvent 0123kl4mn7o568abdefhij9prstuv mulai "
                + "2021-05-21T05:30:00.000+07:00 \n"
                + "\n"
                + "note: jenis data data yang dapat di-update\n "
                + "deskripsi, summary, mulai, dan selesai"
                + "Untuk deskripsi data baru dapat lebih dari dari satu kata";
        assertEquals(command.getHelp(), expected);
    }

    @Test
    public void testGetCommand() {
        assertEquals(command.getCommand(), "updateEvent");
    }
}
