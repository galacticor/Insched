package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class MyAppointmentListCommand implements Command {

    private AppointmentService appointmentService;

    private MessageSender sender = MessageSender.getInstance();

    public MyAppointmentListCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        try {
            if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
                sender.sendPrivateMessage(getHelp(), event);
            } else {
                String idDiscord = event.getAuthor().getId();
                String tanggal = args[0];
                InschedEmbed response = handleEmbed(idDiscord, tanggal);
                sender.sendPrivateMessage(response.build(), event);
            }
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }


    }

    @Override
    public String getCommand() {
        return "myAppointmentList";
    }

    @Override
    public String getHelp() {
        return "!myAppointmentList tanggal\n" + "Contoh: !myAppointmentList 2021-05-03";
    }

    private InschedEmbed handleEmbed(String idDiscord, String tanggal) throws Exception {
        List<Event> eventList = appointmentService.getAllAppointment(idDiscord);
        LocalDate date = LocalDate.parse(tanggal);

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Your Appointment");
        embed.setDescription("Pada tanggal " + tanggal);

        for (Event event : eventList) {
            if (event.getTanggal().isEqual(date)) {
                String desc = event.getDescription();
                String token = "Token: " + event.getIdEvent().toString() + "\n";
                String waktu = event.getWaktu() + "\n";
                String statusBooking = event.getStatusBooking();
                embed.addField(desc, waktu + token + statusBooking, false);
            }
        }
        return embed;
    }
}
