package com.bot.insched.discord.command;

import com.bot.insched.discord.embed.InschedEmbed;
import com.bot.insched.service.EventService;
import com.bot.insched.service.ShowCalendarService;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.time.LocalDate;
import java.util.List;

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

        String userId = event.getMessage().getAuthor().getId();
//        if(service.getCalService(userId)!=null) {
//            String reply = service.getCalService(userId);
//            sendPrivateMessage(reply,event);
//        }
//        else sendPrivateMessage("Ups.. kalender kamu belum ada, masukkan perintah !login", event);
        InschedEmbed response = handleEmbed();
        sendPrivateMessage(response,event);
    }

    public void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }

    private InschedEmbed handleEmbed() {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Calendar Mu : ");

        return embed;
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
