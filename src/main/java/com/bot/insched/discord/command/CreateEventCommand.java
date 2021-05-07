package com.bot.insched.discord.command;

import com.bot.insched.service.DiscordUserService;
import com.bot.insched.service.EventService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateEventCommand implements Command{
    private Event event;
    private EventService eventService;
    private DiscordUserService discordUserService;

    public CreateEventCommand(EventService eventService,
                              DiscordUserService discordUserService){
        this.eventService = eventService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sendPrivateMessage("Selamat Datang di fitur Create Event \n" +
                "Tunggu sebentar,Event anda sedang dibuat",event);
        if (args[0].equalsIgnoreCase("help")) {
            sendPrivateMessage(getHelp(), event);
        } else {
            try{
                String res = handleCreation(args,event.getAuthor().getId());
                sendPrivateMessage(res, event);
            } catch (IndexOutOfBoundsException e) {
                sendPrivateMessage("Masukan argumen yang sesuai!", event);
            } catch (Exception e) {
                sendPrivateMessage(e.toString(),event);
            }
        }
    }

    private String handleCreation(String[] args,String idUser) throws Exception {
        String id = args[0];
        String summary = args[1];
        DateTime dateTimeMulai = new DateTime(args[2] + "T" + args[3] + ":00-07:00");
        EventDateTime eventDateTimeMulai = new EventDateTime().setDateTime(dateTimeMulai)
                .setTimeZone("America/Los_Angeles");
        DateTime dateTimeSelesai = new DateTime(args[4]+"T" + args[5] + ":00-07:00");
        EventDateTime eventDateTimeSelesai = new EventDateTime().setDateTime(dateTimeSelesai)
                .setTimeZone("America/Los_Angeles");
        String deskripsi = args[6];
        event = new Event();
        event.setId(id);
        event.setSummary(summary);
        event.setStart(eventDateTimeMulai);
        event.setEnd(eventDateTimeSelesai);
        event.setDescription(deskripsi);
        return eventService.createEventService(idUser,event);
    }

    @Override
    public String getCommand() {
        return "createEvent";
    }

    @Override
    public String getHelp() {
        return "!createEvent <summary> <token_event> <tanggal_mulai> <jam_mulai> "
                + "<tanggal_selesai> <jam_selesai> <deskripsi_event> \n"
                + "Contoh: !createEvent KUIS 2021-05-20 15:30 2021-05-21 15:30 Kuliah";
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
