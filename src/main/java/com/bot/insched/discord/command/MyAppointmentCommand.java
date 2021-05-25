package com.bot.insched.discord.command;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.AppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyAppointmentCommand implements Command {

    private MessageSender sender = MessageSender.getInstance();
    private AppointmentService appointmentService;

    public MyAppointmentCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        try {
            if (args.length == 0) {
                String discordId = event.getAuthor().getId();
                InschedEmbed response = handleFindAppointment(discordId);
                sender.sendPrivateMessage(response.build(), event);
            }
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "myAppointment";
    }

    @Override
    public String getHelp() {
        return "myAppointment";
    }

    public InschedEmbed handleFindAppointment(String discordId) throws Exception {
        List<Event> eventList = appointmentService.getAllAppointment(discordId);
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Your Appointment Date List");
        Set<LocalDate> date =  new HashSet<>();

        for (Event event: eventList) {
            if (!event.getTanggal().isBefore(LocalDate.now())) {
                date.add(event.getTanggal());
            }
        }

        String listTanggal = "";
        for (LocalDate localDate: date) {
            listTanggal += "- " + localDate.toString() + "\n";
        }
        embed.addField("Berikut adalah list tanggal yang terdapat appointment:", listTanggal, false);
        return embed;
    }
}
