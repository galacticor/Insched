package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.DiscordUserService;
import com.bot.insched.service.EventService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class CreateEventCommand implements Command {
    private Event event;
    private EventService eventService;
    private DiscordUserService discordUserService;
    private MessageSender sender = MessageSender.getInstance();

    public CreateEventCommand(EventService eventService,
                              DiscordUserService discordUserService) {
        this.eventService = eventService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendPrivateMessage(getHelp(), event);
        } else {
            try {
                InschedEmbed embed = new InschedEmbed();
                embed.setTitle("Create Event");
                String res = handleCreation(args, event.getAuthor().getId());
                embed.setDescription(res);
                sender.sendPrivateMessage(embed.build(), event);
            } catch (Exception e) {
                sender.sendPrivateMessage("Input yang anda masukkan salah, "
                        + "Harap mengecek kembali input anda \n"
                        + "!createEvent help (untuk penjelasan penggunaan fitur)", event);
            }
        }
    }

    private String handleCreation(String[] args, String idUser) throws Exception {
        String id = args[0];
        String summary = args[1];
        DateTime dateTimeMulai = new DateTime(args[2] + "T" + args[3] + ":00");
        EventDateTime eventDateTimeMulai = new EventDateTime().setDateTime(dateTimeMulai)
                .setTimeZone("Asia/Jakarta");
        DateTime dateTimeSelesai = new DateTime(args[4] + "T" + args[5] + ":00");
        EventDateTime eventDateTimeSelesai = new EventDateTime().setDateTime(dateTimeSelesai)
                .setTimeZone("Asia/Jakarta");
        String deskripsi = args[6];
        for (int i = 7; i < args.length; i++) {
            deskripsi += " " + args[i];
        }

        event = new Event();
        event.setId(id);
        event.setSummary(summary);
        event.setStart(eventDateTimeMulai);
        event.setEnd(eventDateTimeSelesai);
        event.setDescription(deskripsi);
        return eventService.createEventService(idUser, event);
    }

    @Override
    public String getCommand() {
        return "createEvent";
    }

    @Override
    public String getHelp() {
        return "!createEvent <id_event> <judul> <tanggal_mulai> <jam_mulai> "
                + "<tanggal_selesai> <jam_selesai> <deskripsi_event> <deskripsi_event> ...\n"
                + "Contoh: !createEvent 0123klm4o5678abdefhij9prstuv belajar "
                + "2000-04-22 15:30 2000-05-23 15:30 Kuliah adpro \n"
                + "note: pastikan id event lebih dari 10 karakter yang "
                + "terdiri dari angka dan huruf";
    }

}
