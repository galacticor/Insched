package com.bot.insched.discord.command;

import com.bot.insched.discord.embed.InschedEmbed;
import com.bot.insched.model.Event;
import com.bot.insched.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class MyAppointmentListCommand implements Command {

    private AppointmentService appointmentService;

    public MyAppointmentListCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            sendMessage(getHelp(), event);
        } else {
            String idDiscord = event.getAuthor().getId();
            String tanggal = args[0];
            InschedEmbed response = handleEmbed(idDiscord, tanggal);

            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(response.build()).queue();
            });
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

    private InschedEmbed handleEmbed(String idDiscord, String tanggal) {
        List<Event> eventList = appointmentService.getAllAppointment(idDiscord);
        LocalDate date = LocalDate.parse(tanggal);

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Your Appointment");
        embed.setDescription("Pada tanggal " + tanggal);

        for (Event event : eventList) {
            if (event.getTanggal().isEqual(date)) {
                String desc = event.getDescription();
                String waktu = event.getWaktu() + "\n";
                String statusBooking = event.getStatusBooking();
                embed.addField(desc, waktu + statusBooking, false);
            }
        }
        return embed;
    }

    private void sendMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }

}
