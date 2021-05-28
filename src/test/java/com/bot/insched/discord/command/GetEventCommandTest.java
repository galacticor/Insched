package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.DiscordUser;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GetEventCommandTest {
    @Mock
    private EventService eventService;

    @Mock
    private PrivateMessageReceivedEvent event;

    @InjectMocks
    private GetEventCommand command;

    @Mock
    PrivateChannel privateChannel;

    @Mock
    private MessageSender sender;


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
        PrivateChannel channel = mock(PrivateChannel.class);
        MessageAction action = mock(MessageAction.class);
        String[] args = {"!getEvent","0123456789abcdefghijklmnopqrstuv"};
        lenient().when(eventService.getCalendarbyId(anyString())).thenReturn(calendar);
        lenient().when(eventService.deleteEventService(any(), any()))
                .thenReturn("Event Berhasil dihapus");
        lenient().when(event.getChannel()).thenReturn(channel);
        lenient().when(channel.sendMessage(anyString())).thenReturn(action);
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
        String[] args = {"cek"};
        lenient().when(event.getAuthor()).thenReturn(null);
        command.execute(args, event);
    }


    @Test
    public void testFalseArgument() {
        PrivateChannel channel = mock(PrivateChannel.class);
        MessageAction action = mock(MessageAction.class);
        String[] args = {"dummy"};
        lenient().when(event.getChannel()).thenReturn(channel);
        lenient().when(channel.sendMessage(anyString())).thenReturn(action);
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        PrivateChannel channel = mock(PrivateChannel.class);
        MessageAction action = mock(MessageAction.class);
        String expected = "!getEvent <idEvent>\n" +
                "Contoh: !getEvent absfuoqebfojdbvqe";
        lenient().when(privateChannel.sendMessage(expected)).thenReturn(mock(MessageAction.class));
        lenient().when(privateChannel.sendMessage(expected).toString()).thenReturn("cek");
        lenient().when(event.getChannel()).thenReturn(channel);
        lenient().when(channel.sendMessage(anyString())).thenReturn(action);
        assertEquals(command.getHelp(), expected);
    }

    @Test
    public void testGetCommand(){
        assertEquals(command.getCommand(),"getEvent");
    }
}
