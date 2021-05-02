package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import com.bot.insched.service.GoogleServiceImpl;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class AuthCommand implements Command {
	private GoogleService service;

    public AuthCommand(){
        service = new GoogleServiceImpl();
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
        return "auth";
    }
}
