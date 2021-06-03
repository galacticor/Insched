package com.bot.insched.discord.task;

import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class NotificationTask implements Runnable {
    private MessageSender sender = MessageSender.getInstance();
    private MessageEmbed message;
    private String userId;

    public NotificationTask(MessageEmbed message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    @Override
    public void run() {
        sender.sendPrivateNotificationById(message, userId);
    }
}