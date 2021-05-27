package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.ShowCalendarService;
import com.google.api.services.calendar.model.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class ShowCalendarCommand implements Command {
    private ShowCalendarService service;
    private MessageSender sender = MessageSender.getInstance();

    public ShowCalendarCommand(ShowCalendarService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sender.sendPrivateMessage("Selamat datang di fitur ShowCalendar! "
                + "Di bawah ini adalah kalender kamu", event);

        String userId = event.getMessage().getAuthor().getId();
        sender.sendPrivateMessage(createEmbed(userId, event).build(), event);
    }

    public InschedEmbed createEmbed(String userId, PrivateMessageReceivedEvent event) {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("CalendarMu");
        try {
            for (Event events: service.getListEvents(userId)) {
                String summary = service.getCalSummary(events);
                String description = service.getCalDescription(events);
                String startTime = service.getCalStart(events);
                String endTime = service.getCalEnd(events);
                embed.addField("📅  " + summary,String.format("Date : %s  \n Time : %s  -  %s ",
                        startTime.substring(0,10), startTime.substring(11,16),
                        endTime.substring(11,16)),false);
            }
        } catch (Exception e) {
            e.printStackTrace();
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


