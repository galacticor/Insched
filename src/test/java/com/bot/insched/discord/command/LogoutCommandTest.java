package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.GoogleService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.MessageBuilder;
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
public class LogoutCommandTest {
    @InjectMocks
    LogoutCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    GoogleService googleService;

    @Mock
    DiscordUserService discordService;

    @Mock
    private MessageSender sender;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(command, "sender", sender);
    }

    @Test
    public void testExecute() {
        String[] args = {"logout"};
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Good Bye");
        embed.setDescription(String.format("Terimakasih sudah menggunakan bot Insched, selamat tinggal %s", "userInfo"));
        User user = mock(User.class);

        when(googleService.getUserInfo("123")).thenReturn("userInfo");
        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("123");
        doNothing().when(discordService).logout("123");
        doNothing().when(sender).sendPrivateMessage(embed.build(), event);

        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        assertEquals(res, null);
    }

    @Test
    public void testGetCommand(){
        assertEquals(command.getCommand(),"logout");
    }
}
