package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.DiscordUserService;
import com.bot.insched.service.EventService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class UpdateEventCommand implements Command {
    private Event event;
    private EventService eventService;
    private DiscordUserService discordUserService;
    private MessageSender sender = MessageSender.getInstance();

    public UpdateEventCommand(EventService eventService,
                              DiscordUserService discordUserService) {
        this.eventService = eventService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sender.sendPrivateMessage("Selamat Datang di fitur Create Event \n"
                + "Tunggu sebentar,Event anda sedang di-update", event);
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendPrivateMessage(getHelp(), event);
        } else {
            try {
                InschedEmbed embed = new InschedEmbed();
                embed.setTitle("Update Event");
                String res = handleCreation(args, event.getAuthor().getId());
                embed.setDescription(res);
                sender.sendPrivateMessage(embed.build(), event);
            } catch (IndexOutOfBoundsException e) {
                sender.sendPrivateMessage("Masukan argumen yang sesuai!", event);
            } catch (Exception e) {
                sender.sendPrivateMessage(e.toString(), event);
            }
        }
    }

    private String handleCreation(String[] args, String idUser) throws Exception {
        String idEvent = args[0];
        String jenis = args[1];
        String newData = args[2];
        return eventService.updateEventService(idUser, idEvent, jenis, newData);
    }

    @Override
    public String getCommand() {
        return "updateEvent";
    }

    @Override
    public String getHelp() {
        return "!updateEvent <eventID> <jenis> <dataBaru> \n"
                + "Contoh: !updateEvent 0123kl4mn7o568abdefhij9prstuv mulai 2021-05-21T05:30:00.000+07:00 \n"
                + "note: jenis data data yang dapat di-update\n "
                + "deskripsi, summary, mulai, dan selesai";
    }

}
