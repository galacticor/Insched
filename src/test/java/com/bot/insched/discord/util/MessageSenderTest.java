package com.bot.insched.discord.util;

import com.bot.insched.discord.util.InschedEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageSenderTest {

	@Mock
	private PrivateMessageReceivedEvent event;
	
	@Mock
	private MessageSender sender;

	private static JDA jda;
	private static String userId = "567662715317780481";
	private static User jdaUser;

	@BeforeAll
    public static void init() throws Exception {
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
        	jdaUser = user;
        });
        Thread.sleep(2000);
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

	@BeforeEach
	public void setUp() throws Exception {
		event = mock(PrivateMessageReceivedEvent.class);
		sender = MessageSender.getInstance();
		sender.configure();
		ReflectionTestUtils.setField(sender, "jda", jda);

		Thread.sleep(1000);
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

	@Test
	public void testSendPrivateNotificationById() {
		String message1 = "dummy message";
		sender.sendPrivateNotificationById(message1, userId);

		MessageEmbed message2 = mock(MessageEmbed.class);
		sender.sendPrivateNotificationById(message2, userId);
	}

	@Test
	public void testSendPrivateNotificationString() {
		String message1 = "dummy message";
		sender.sendPrivateNotification(message1, jdaUser);
	}

	@Test
	public void testSendPrivateNotificationEmbed() {
		InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Dummy");

		sender.sendPrivateNotification(embed.build(), jdaUser);
	}
}