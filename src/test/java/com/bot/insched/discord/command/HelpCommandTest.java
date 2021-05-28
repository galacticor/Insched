package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HelpCommandTest {
    @InjectMocks
    HelpCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    private MessageSender sender;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(command, "sender", sender);
    }

    @Test
    public void testExecute() {
        String[] args = {"help"};
        doNothing().when(sender).sendPrivateMessage(command.getHelp(), event);

        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        assertNotEquals(res, null);
    }

    @Test
    public void testGetCommand(){
        assertEquals(command.getCommand(),"help");
    }
}
