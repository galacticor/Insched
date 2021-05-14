package com.bot.insched.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class MessageSender {
	private static MessageSender instance = new MessageSender();

	private MessageSender(){}

    public static MessageSender getInstance() {
        return instance;
    }

    public void sendPrivateMessage(MessageEmbed message, PrivateMessageReceivedEvent event) {
    	event.getChannel().sendMessage(message).queue();
    }

    public void sendPrivateMessage(String message, PrivateMessageReceivedEvent event) {
    	event.getChannel().sendMessage(message).queue();
    }
}
