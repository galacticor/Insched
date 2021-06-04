package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.ShowCalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class ShowCalendarCommand implements Command {
    private ShowCalendarService service;
    private MessageSender sender = MessageSender.getInstance();

    public ShowCalendarCommand(ShowCalendarService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sender.sendPrivateMessage("Selamat datang di fitur ShowCalendar !", event);

        String userId = event.getMessage().getAuthor().getId();
        sender.sendPrivateMessage(createEmbed(args,userId, event).build(), event);

        if (args.length == 0) {
            InschedEmbed embed2 = new InschedEmbed();
            embed2.setColor(12745742);
            embed2.addField(" ","Selain itu kamu juga bisa memilih tanggal "
                    + "mulai dan selesai "
                    + "untuk kalender mu. Formatnya adalah `!showCalendar YYYY-MM-DD "
                    + "sampai YYYY-MM-DD`."
                    + " Namun kalender ini hanya bisa menampilkan maksimal 10 event ", false);

            sender.sendPrivateMessage(embed2.build(), event);
        }
    }

    public InschedEmbed createEmbed(String[] args, String userId,
                                    PrivateMessageReceivedEvent event) {
        List<Event> listEvent = new ArrayList<>();

        InschedEmbed embed = new InschedEmbed();
        embed.setColor(15844367);
        embed.setTitle("CalendarMu");
        if (args.length == 0) {
            try {
                listEvent = service.getListEvents(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                DateTime timeMulai = new DateTime(args[0] + "T01:00:00.001+07:00");
                DateTime timeSelesai = new DateTime(args[2] + "T01:00:00.001+07:00");
                listEvent = service.getListEvents(userId, timeMulai, timeSelesai);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Event events: listEvent) {
            String summary = service.getCalSummary(events);
            String description = service.getCalDescription(events);
            String startTime = service.getCalStart(events);
            String endTime = service.getCalEnd(events);
            embed.addField("📅  " + summary, String.format("Date : %s  \n Time : %s  -  %s ",
                    startTime.substring(0, 10), startTime.substring(11, 16),
                    endTime.substring(11, 16)), false);
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


