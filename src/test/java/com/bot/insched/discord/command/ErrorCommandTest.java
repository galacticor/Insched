package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.annotation.Order;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Array;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ErrorCommandTest {
    @InjectMocks
    ErrorCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    private MessageSender sender;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(command, "sender", sender);
    }

    @Test
    public void testErrorExecute() {
        String[] args = {"error"};
        // setiap sendPrivateMessage, kalian cman perlu mock ini aja sebaris, dia bisa nerima MessageEmbed atau String class
        doNothing().when(sender).sendPrivateMessage(command.getHelp(), event);

        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        String expected = "Command yang anda masukkan Salah \n" +
                "!help untuk mengetahui command setiap fitur yang tersedia";
        assertEquals(command.getHelp(), expected);
        assertEquals(res, expected);
    }

    @Test
    public void testGetCommand(){
        assertEquals(command.getCommand(),"error");
    }
}
