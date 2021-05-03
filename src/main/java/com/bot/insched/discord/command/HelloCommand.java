package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelloCommand implements Command{

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Hello!").queue();
        });
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
