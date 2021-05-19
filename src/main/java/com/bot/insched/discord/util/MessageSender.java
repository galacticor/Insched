package com.bot.insched.discord.util;

import com.bot.insched.InschedApplication;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class MessageSender {
    private static MessageSender instance = new MessageSender();
    private static JDA jda;

    private MessageSender() {
    }

    public static MessageSender getInstance() {
        return instance;
    }

    public static void configure() {
        if (jda == null) {
            jda = InschedApplication.getJda();
        }
    }

    /*
        Send Private Message with event and message as Embed or String
        Required: @event and @message
    */
    public void sendPrivateMessage(MessageEmbed message, PrivateMessageReceivedEvent event) {
        event.getChannel().sendMessage(message).queue();
    }

    public void sendPrivateMessage(String message, PrivateMessageReceivedEvent event) {
        event.getChannel().sendMessage(message).queue();
    }

    /*
        Send Private Message as Notification without event
        and require userId with message as Embed or String
        Required: @userId and @message
    */
    public void sendPrivateNotificationById(String message, String userId) {
        configure();
        jda.retrieveUserById(userId).queue(user -> {
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(message).queue();
            });
        });
    }

    public void sendPrivateNotificationById(MessageEmbed message, String userId) {
        configure();
        jda.retrieveUserById(userId).queue(user -> {
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(message).queue();
            });
        });
    }

    /*
        Send Private Message as Notification without event
        and require user as JDA User with message as Embed or String
        Required: @user and @message
    */
    public void sendPrivateNotification(String message, User user) {
        configure();
        user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(message).queue();
        });
    }

    public void sendPrivateNotification(MessageEmbed message, User user) {
        configure();
        user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(message).queue();
        });
    }
}
