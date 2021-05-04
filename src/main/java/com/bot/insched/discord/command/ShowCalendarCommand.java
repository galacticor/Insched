package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import com.bot.insched.service.EventService;
import net.dv8tion.jda.api.events.Event;

public class ShowCalendarCommand implements Command{
    private Event event;
    private EventService eventService;
    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Ini adalah fitur untuk melihat Calendar mu !").queue();
        });

    }

    @Override
    public String getCommand() {
        return "showCalendar";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
