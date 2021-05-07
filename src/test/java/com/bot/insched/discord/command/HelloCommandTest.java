package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class HelloCommandTest {
    @InjectMocks
    HelloCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    GoogleService googleService;
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
        Thread.sleep(2000);
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

    @Test
    public void testErrorExecute() {
        String[] args = {"hello"};
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
        lenient().when(event.getMessage()).thenReturn(message);
        lenient().when(event.getMessage()).thenReturn(mock(Message.class));
        lenient().when(event.getMessage().getAuthor()).thenReturn(mock(User.class));
        lenient().when(event.getMessage().getAuthor().getId()).thenReturn("id");
        lenient().when(googleService.getUserInfo("id")).thenReturn("User");
        command.execute(args,event);
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        assertEquals(res, null);
    }

    @Test
    public void testGetCommand(){
        assertEquals(command.getCommand(),"hello");
    }

    @Test
    public void testSendPrivateMessage(){

    }
}
