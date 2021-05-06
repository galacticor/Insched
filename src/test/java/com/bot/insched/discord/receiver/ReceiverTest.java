package com.bot.insched.discord.receiver;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message;
import com.bot.insched.discord.command.Command;
import com.bot.insched.discord.command.HelloCommand;
import com.bot.insched.discord.command.errorCommand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ReceiverTest {
	@Mock
	PrivateMessageReceivedEvent event;

	@Mock
	Message message;

	@InjectMocks
	Receiver receiver;

	private String commandMessage = "!hello";
	private String unknownMessage = "!lol message";

	@BeforeEach
    public void setUp() {
    	
    }

    // @Test
    // public void testExecuteSuccess(){
    // 	HelloCommand hello = mock(HelloCommand.class);
    // 	Command error = mock(errorCommand.class);
    // 	receiver.addCommand(hello);
    // 	receiver.addCommand(error);

    // 	String[] args = new String[]{"message"};
    // 	when(event.getMessage()).thenReturn(message);
    // 	when(message.getContentRaw()).thenReturn(commandMessage);
    // 	doNothing().when(hello).execute(args, event);

    // 	receiver.execute(event);
    // }

    // @Test
    // public void testExecuteFailed(){
    // 	String[] args = new String[]{"message"};
    // 	when(event.getMessage()).thenReturn(message);
    // 	when(message.getContentRaw()).thenReturn(unknownMessage);
    // 	doNothing().when(errorCommand).execute(args, event);

    // 	receiver.execute(event);
    // }

}