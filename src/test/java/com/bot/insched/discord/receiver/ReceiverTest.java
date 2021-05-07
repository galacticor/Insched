package com.bot.insched.discord.receiver;

import com.bot.insched.discord.command.Command;
import com.bot.insched.discord.command.HelloCommand;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiverTest {

    @InjectMocks
    Receiver receiver;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    HelloCommand helloCommand;

    @BeforeEach
    public void setUp() throws Exception {
        Map<String, Command> commands = new HashMap<>();
        commands.put("success", helloCommand);
        commands.put("error", helloCommand);

        // Get field instance
        Field field = Receiver.class.getDeclaredField("commands");
        field.setAccessible(true); // Suppress Java language access checking

        // Remove "final" modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set value
        field.set(receiver, commands);
    }

    @Test
    public void testExecuteSuccess() {
        Message message = new MessageBuilder().append("!success").build();
        when(event.getMessage()).thenReturn(message);
        doNothing().when(helloCommand).execute(any(String[].class), any(PrivateMessageReceivedEvent.class));

        receiver.execute(event);
    }

    @Test
    public void testExecuteFailed() {
        Message message = new MessageBuilder().append("!haha").build();
        when(event.getMessage()).thenReturn(message);
        doNothing().when(helloCommand).execute(any(String[].class), any(PrivateMessageReceivedEvent.class));

        receiver.execute(event);
    }

    @Test
    public void testExecuteFalse() {
        Message message = new MessageBuilder().append("haha").build();
        when(event.getMessage()).thenReturn(message);

        receiver.execute(event);
    }
}
