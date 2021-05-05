package com.bot.insched.discord.command;

import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class CreateAppointmentCommand implements Command{

    private AppointmentService  appointmentService;
    private DiscordUserService discordUserService;

    public CreateAppointmentCommand(AppointmentService appointmentService,
            DiscordUserService discordUserService) {
        this.appointmentService = appointmentService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {

        if (args[0].equalsIgnoreCase("help")) {
            sendPrivateMessage(getHelp(), event);
        } else {
            try {
                String desc = args[0];
                String startDate = args[1];
                String endDate = args[2];
                String discordId = event.getAuthor().getId();
                String response = appointmentService.createAppointment(desc, startDate, endDate, discordId);
                sendPrivateMessage(response, event);

            } catch (Exception e) {
                System.out.println(e.toString());
                sendPrivateMessage("Argumen tidak valid. Gunakan perintah !createAppointment help untuk melihat detail argumen", event);
            }
        }

    }

    @Override
    public String getCommand() {
        return "createAppointment";
    }

    @Override
    public String getHelp() {
        return "!createAppointment <deskripsi_appointment> <tanggal_mulai> <tanggal_selesai>\n" +
                "Contoh: !createAppointment DEMO_TP3 2021-05-03 2021-05-15";
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }

}
