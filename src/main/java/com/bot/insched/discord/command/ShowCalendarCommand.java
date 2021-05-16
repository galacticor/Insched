package com.bot.insched.discord.command;

import com.bot.insched.discord.embed.InschedEmbed;
import com.bot.insched.service.ShowCalendarService;
import com.google.api.services.calendar.model.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

public class ShowCalendarCommand implements Command {
    private ShowCalendarService service;
    String userId;
    List<Event> listEvent;

    public ShowCalendarCommand(ShowCalendarService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sendPrivateMessage("Selamat Datang di fitur showCalendar", event);
        sendPrivateMessage(event.getMessage().getAuthor().getId(), event);
        sendPrivateMessage(event.getAuthor().getId(), event);
        userId = event.getMessage().getAuthor().getId();

//        if(service.getCalService(userId)!=null) {
//            String reply = service.getCalService(userId);
//            sendPrivateMessage(reply,event);
//        }
//        else sendPrivateMessage("Ups.. kalender kamu belum ada, masukkan perintah !login", event);
        InschedEmbed response = handleEmbed();
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response.build()).queue();
        });
    }

    public List<Event> getListEvent(){
        listEvent = service.getEvents(userId);
        return listEvent;
    }

    public void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }

    private InschedEmbed handleEmbed() {
        InschedEmbed embed = new InschedEmbed();
        String summary;
        String description;
        embed.setTitle("Calendar Mu : ");
        for (Event events : getListEvent()) {
            summary = service.getCalSummary(events);
            description = service.getCalDescription(events);
            embed.addField("Event : ", summary, false);
            embed.addField("Description : ", description, false);
        }
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


