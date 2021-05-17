package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelloCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();
    private GoogleService googleService;

    public HelloCommand(GoogleService googleService) {
        this.googleService = googleService;
    }


    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        String reply = "Hello " + googleService.getUserInfo(userId) + "!!";
        sender.sendPrivateMessage(reply, event);
    }

    @Override
    public String getCommand() {
        return "hello";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
