package com.bot.insched.discord.command;

import com.bot.insched.service.EventService;
import com.bot.insched.service.ShowCalendarService;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class ShowCalendarCommand implements Command {
    Event event;
    EventService eventService;
    private ShowCalendarService service;

    public ShowCalendarCommand(ShowCalendarService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sendPrivateMessage("Selamat Datang di fitur showCalendar", event);
        sendPrivateMessage(event.getMessage().getAuthor().getId(), event);
        sendPrivateMessage(event.getAuthor().getId(), event);
    }

    public void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
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
