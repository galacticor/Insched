package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.DiscordUserService;
import com.bot.insched.service.EventService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class DeleteEventCommand implements Command {
    private Event event;
    private EventService eventService;
    private DiscordUserService discordUserService;
    private MessageSender sender = MessageSender.getInstance();

    public DeleteEventCommand(EventService eventService,
                              DiscordUserService discordUserService) {
        this.eventService = eventService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sender.sendPrivateMessage("Selamat Datang di fitur Create Event \n"
                + "Tunggu sebentar,Event anda sedang dihapus", event);
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendPrivateMessage(getHelp(), event);
        } else {
            try {
                String res = handleCreation(args, event.getAuthor().getId());
                sender.sendPrivateMessage(res, event);
            } catch (Exception e) {
                sender.sendPrivateMessage("Masukan argumen yang sesuai!", event);
            }
        }
    }

    private String handleCreation(String[] args, String idUser) throws Exception {
        String eventId = args[0];
        return eventService.deleteEventService(idUser, eventId);
    }

    @Override
    public String getCommand() {
        return "deleteEvent";
    }

    @Override
    public String getHelp() {
        return "!deleteEvent <idEvent>\n"
                + "Contoh: !deleteEvent absfuoqebfojdbvqe";
    }

}
