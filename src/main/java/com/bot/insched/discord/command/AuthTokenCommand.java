package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AuthTokenCommand implements Command {

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
    	
    }

    @Override
    public String getCommand() {
        return "auth-token";
    }
}
