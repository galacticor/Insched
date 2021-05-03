package com.bot.insched.discord.command;

import com.bot.insched.InschedApplication;
import com.bot.insched.service.GoogleService;
import com.bot.insched.service.GoogleServiceImpl;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelloCommand implements Command{
    private GoogleService googleService;

    public HelloCommand (GoogleService googleService){
        this.googleService = googleService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getMessage().getAuthor().getId();
        String reply = "Hello " + googleService.getUserInfo(userId) + "!!";
        
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(reply).queue();
        });
    }

    @Override
    public String getCommand() {
        return "hello";
    }
}
