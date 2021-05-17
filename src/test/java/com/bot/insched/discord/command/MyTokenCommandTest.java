package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Mock
    private MessageSender sender;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(myTokenCommand, "sender", sender);
    }

    @Test
    public void testTokenNotExists() {
        String[] args = {};
        User user = mock(User.class);
        String res = "Kamu belum login. Silahkan login menggunakan !login";

        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("123");
        when(discordUserService.findByUserId(anyString())).thenReturn(null);
        doNothing().when(sender).sendPrivateMessage(res, event);
        myTokenCommand.execute(args, event);
    }

    @Test
    public void testTokenExists() {
        String[] args = {};
        DiscordUser dummy = new DiscordUser();
        User user = mock(User.class);
        String res = res = "Token kamu adalah: dummy token";

        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("123");
        when(discordUserService.findByUserId(anyString())).thenReturn(dummy);
        when(appointmentService.getUserToken(any())).thenReturn("dummy token");
        doNothing().when(sender).sendPrivateMessage(res, event);

        myTokenCommand.execute(args, event);
    }

    @Test
    public void testGetCommand() {
        String res = myTokenCommand.getCommand();
        assertEquals(res, "myToken");
    }

    @Test
    public void testGetHelp() {
        String res = myTokenCommand.getHelp();
        assertEquals(res, "!myToken");
    }

}
