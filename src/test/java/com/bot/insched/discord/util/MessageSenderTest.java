package com.bot.insched.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class MessageSenderTest {

	@Mock
	private PrivateMessageReceivedEvent event;
	@Mock
	private MessageSender sender;

	@BeforeEach
	public void setUp(){

		event = mock(PrivateMessageReceivedEvent.class);
		sender = MessageSender.getInstance();
	}

	@Test
	public void testOnlyCreatedOnce() {
		MessageSender sender1 = MessageSender.getInstance();
		MessageSender sender2 = MessageSender.getInstance();

		assertThat(sender1).isEqualToComparingFieldByField(sender2);
	}

	@Test
	public void testSendPrivateMessage() {
		String message1 = "dummy message";

		PrivateChannel channel = mock(PrivateChannel.class);
		MessageAction action = mock(MessageAction.class);
		MessageSender senderMock = MessageSender.getInstance();

		when(event.getChannel()).thenReturn(channel);
		when(channel.sendMessage(anyString())).thenReturn(action);
		doNothing().when(action).queue();

		sender.sendPrivateMessage(message1, event);

		MessageEmbed message2 = mock(MessageEmbed.class);

		when(event.getChannel()).thenReturn(channel);
		when(channel.sendMessage(any(MessageEmbed.class))).thenReturn(action);
		doNothing().when(action).queue();

		sender.sendPrivateMessage(message2, event);
	}
}