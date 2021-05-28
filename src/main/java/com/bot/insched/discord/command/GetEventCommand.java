package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.DiscordUserService;
import com.bot.insched.service.EventService;
import com.google.api.services.calendar.model.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class GetEventCommand implements Command{
    private Event event;
    private EventService eventService;
    private DiscordUserService discordUserService;
    private MessageSender sender = MessageSender.getInstance();

    public GetEventCommand(EventService eventService,
                              DiscordUserService discordUserService) {
        this.eventService = eventService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sender.sendPrivateMessage("Selamat Datang di fitur Get Event \n"
                + "Tunggu sebentar,Event anda sedang dicari :slight_smile:", event);
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendPrivateMessage(getHelp(), event);
        } else {
            try {
                InschedEmbed embed = new InschedEmbed();
                embed.setTitle("Get Event");
                String res = handleCreation(args, event.getAuthor().getId());
                embed.setDescription(res);
                sender.sendPrivateMessage(embed.build(), event);
            } catch (Exception e) {
                sender.sendPrivateMessage("Masukan argumen yang sesuai! :smiley:", event);
            }
        }
    }

    private String handleCreation(String[] args, String idUser) throws Exception {
        String eventId = args[0];
        return eventService.getEventIdService(idUser, eventId);
    }

    @Override
    public String getCommand() {
        return "getEvent";
    }

    @Override
    public String getHelp() {
        return "!getEvent <idEvent>\n"
                + "Contoh: !getEvent absfuoqebfojdbvqe";
    }
}
