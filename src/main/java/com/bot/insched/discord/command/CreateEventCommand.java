package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class CreateEventCommand implements Command {
    

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Selamat Datang di fitur Create Event").queue();
        });
    }

    @Override
    public String getCommand() {
        return "createEvent";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
