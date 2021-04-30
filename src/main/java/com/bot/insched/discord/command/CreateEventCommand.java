package com.bot.insched.discord.command;

import com.bot.insched.service.EventService;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class CreateEventCommand implements Command{
    private Event event;
    private EventService eventService;
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
}
