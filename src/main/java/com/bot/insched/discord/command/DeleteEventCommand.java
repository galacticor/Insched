package com.bot.insched.discord.command;

import com.bot.insched.service.DiscordUserService;
import com.bot.insched.service.EventService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class DeleteEventCommand implements Command{
    private Event event;
    private EventService eventService;
    private DiscordUserService discordUserService;

    public DeleteEventCommand(EventService eventService,
                              DiscordUserService discordUserService){
        this.eventService = eventService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sendPrivateMessage("Selamat Datang di fitur Create Event \n"
                + "Tunggu sebentar,Event anda sedang dibuat",event);
        if (args[0].equalsIgnoreCase("help")) {
            sendPrivateMessage(getHelp(), event);
        } else {
            try{
                String res = handleCreation(args,event.getAuthor().getId());
                sendPrivateMessage(res, event);
            }catch (Exception e){
                sendPrivateMessage(e.toString(),event);
            }
        }
    }

    private String handleCreation(String[] args,String idUser) throws Exception {
        String eventId = args[0];
        return eventService.deleteEventService(idUser,eventId);
    }

    @Override
    public String getCommand() {
        return "deleteEvent";
    }

    @Override
    public String getHelp() {
        return "!deleteEvent <idEvent>\n" +
                "Contoh: !deleteEvent absfuoqebfojdbvqe";
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
