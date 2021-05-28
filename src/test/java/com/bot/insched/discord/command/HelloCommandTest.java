package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HelloCommandTest {
    @InjectMocks
    HelloCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    GoogleService googleService;
    @Mock
    private Message message;
    @Mock
    private User jdaUser;
    @Mock
    private MessageSender sender;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(command, "sender", sender);
    }

    @Test
    public void testExecute() {
        String[] args = {"hello"};

        when(event.getAuthor()).thenReturn(jdaUser);
        when(jdaUser.getId()).thenReturn("123");
        when(googleService.getUserInfo(anyString())).thenReturn("User");
        doNothing().when(sender).sendPrivateMessage("Hello User!!", event);

        command.execute(args, event);
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
}
