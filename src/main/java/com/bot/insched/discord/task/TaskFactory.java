package com.bot.insched.discord.task;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class TaskFactory {
	
	public NotificationTask newNotificationTask(MessageEmbed message, String userId) {
		return new NotificationTask(message, userId);
	}
}