package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelloCommand implements Command {
    private GoogleService googleService;

    public HelloCommand(GoogleService googleService) {
        this.googleService = googleService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getMessage().getAuthor().getId();
        String reply = "Hello " + googleService.getUserInfo(userId) + "!!";
        sendPrivateMessage(reply,event);
    }

    @Override
    public String getCommand() {
        return "hello";
    }

    @Override
    public String getHelp() {
        return null;
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
