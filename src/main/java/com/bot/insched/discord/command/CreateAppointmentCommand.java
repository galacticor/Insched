package com.bot.insched.discord.command;

import com.bot.insched.service.EventService;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class CreateAppointmentCommand implements Command{
    private Event event;
    private EventService eventService;
    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Selamat Datang fitur create Appointment !").queue();
        });

    }

    @Override
    public String getCommand() {
        return "createAppointment";
    }
}
