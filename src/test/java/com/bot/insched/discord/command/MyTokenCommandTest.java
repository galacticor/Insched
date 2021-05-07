package com.bot.insched.discord.command;

import com.bot.insched.model.DiscordUser;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyTokenCommandTest {

    @InjectMocks
    MyTokenCommand myTokenCommand;

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
        Thread.sleep(2000);

    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }


    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
    }

    @Test
//    @Order(1)
    public void testTokenNotExists() {
        String[] args = {};
        when(discordUserService.findByUserId(anyString())).thenReturn(null);
        myTokenCommand.execute(args, event);
    }

    @Test
//    @Order(2)
    public void testTokenExists() {
        String[] args = {};
        DiscordUser dummy = new DiscordUser();
        when(discordUserService.findByUserId(anyString())).thenReturn(dummy);
        when(appointmentService.getUserToken(any())).thenReturn("dummy token");

        myTokenCommand.execute(args, event);
    }

    @Test
//    @Order(3)
    public void testGetCommand() {
        String res = myTokenCommand.getCommand();
        assertEquals(res, "myToken");
    }

    @Test
//    @Order(4)
    public void testGetHelp() {
        String res = myTokenCommand.getHelp();
        assertEquals(res, "!myToken");
    }

}
