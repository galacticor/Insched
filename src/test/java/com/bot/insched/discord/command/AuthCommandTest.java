package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthCommandTest {
    @InjectMocks
    AuthCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    GoogleService googleService;

    @Mock
    private MessageSender sender;

    @Mock
    private User jdaUser;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(command, "sender", sender);
    }

    @Test
    public void testExecute() {
        String[] args = {"token"};
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Authentication Status");
        embed.setDescription("success");

        when(event.getAuthor()).thenReturn(jdaUser);
        when(jdaUser.getId()).thenReturn("123");
        when(googleService.authToken("123", "token")).thenReturn("success");
        doNothing().when(sender).sendPrivateMessage(embed.build(), event);

        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "Masukkan dengan format seperti berikut \n" +
            "!auth <token>";
        assertEquals(command.getHelp(), expected);
    }

    @Test
    public void testGetCommand(){
        assertEquals(command.getCommand(),"auth");
    }
}
