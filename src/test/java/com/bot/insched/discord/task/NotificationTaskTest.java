package com.bot.insched.discord.task;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationTaskTest {
	@InjectMocks
	private NotificationTask task;
	@Mock
	private MessageSender sender;

	private MessageEmbed message;
	private TaskFactory factory = new TaskFactory();
	private String userId = "123";

	@BeforeEach
	public void setUp() {
		InschedEmbed embed = new InschedEmbed();
		message = embed.build();
		
		ReflectionTestUtils.setField(task, "sender", sender);
		ReflectionTestUtils.setField(task, "userId", userId);
		ReflectionTestUtils.setField(task, "message", message);
	}

	@Test
	public void testRun() {
		doNothing().when(sender).sendPrivateNotificationById(message, "123");

		task.run();
	}

	@Test
	public void testCreation() {
		assertTrue(factory.newNotificationTask(message, userId) instanceof NotificationTask);
	}
}