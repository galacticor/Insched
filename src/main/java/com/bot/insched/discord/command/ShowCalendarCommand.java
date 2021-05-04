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
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Selamat Datang di fitur showCalendar").queue();
        });

        String userId = event.getMessage().getAuthor().getId();
        if(service.getCalService(userId)!=null) {
            String reply = service.getCalService(userId);
            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(reply).queue();
            });
        }
        else event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Ups.. kalender kamu belum ada, masukkan perintah !login untuk login").queue();
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
