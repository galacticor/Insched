package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AuthTokenCommand implements Command {
	private GoogleService service;

    public AuthTokenCommand(GoogleService service){
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
    	String userId = event.getMessage().getAuthor().getId();
    	String reply = service.authToken(userId, args[0]);

    	event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(reply).queue();
        });
    }

    @Override
    public String getCommand() {
        return "auth";
    }
}
