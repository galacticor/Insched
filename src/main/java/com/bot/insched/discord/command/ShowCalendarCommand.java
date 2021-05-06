package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import com.bot.insched.service.ShowCalendarService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import com.bot.insched.service.EventService;
import net.dv8tion.jda.api.events.Event;

public class ShowCalendarCommand implements Command{
    private Event event;
    private EventService eventService;
    private ShowCalendarService service;

    public ShowCalendarCommand(ShowCalendarService service){
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sendPrivateMessage("Selamat Datang di fitur showCalendar", event);
        sendPrivateMessage(event.getMessage().getAuthor().getId(), event);

        String userId = event.getMessage().getAuthor().getId();
        if(service.getCalService(userId)!=null) {
            String reply = service.getCalService(userId);
            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(reply).queue();
            });
        }
        else sendPrivateMessage("Ups.. kalender kamu belum ada, masukkan perintah !login", event);


    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    };


    @Override
    public String getCommand() {
        return "showCalendar";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
