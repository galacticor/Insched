package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AuthCommand implements Command {
	private GoogleService service;

    public AuthCommand(GoogleService service){
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String url = service.getAuthorizationUrl();
    	event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(url).queue();
        });
    }

    @Override
    public String getCommand() {
        return "login";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
