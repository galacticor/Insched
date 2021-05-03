package com.bot.insched.discord.command;

import com.bot.insched.discord.embed.InschedEmbed;
import com.bot.insched.model.Appointment;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

public class ShowMyAppointment implements Command{

    private AppointmentService appointmentService;
    private DiscordUserService discordUserService;

    public ShowMyAppointment(
            AppointmentService appointmentService,
            DiscordUserService discordUserService) {
        this.appointmentService = appointmentService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String idDiscord = event.getAuthor().getId();
        List<Appointment> listAppointment = appointmentService.getAllUserAppointment(idDiscord);

        if (args.length != 0 && args[0].equalsIgnoreCase("help")) {
            sendPrivateMessage("test help", event);
        } else {
            InschedEmbed info = new InschedEmbed();
            info.setTitle("Your Appointment List");
            for (Appointment elem: listAppointment){
                String desc = elem.getDescription();
                String startDate = String.format("Start date: %s \n", elem.getStartDate().toString());
                String endDate = String.format("End date: %s \n", elem.getEndDate().toString());
                String token = String.format("Token: %s \n", elem.getIdAppointment());
                info.addField(elem.getDescription(), startDate + endDate + token, false);
            }

            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(info.build()).queue();
            });
        }

    }

    @Override
    public String getCommand() {
        return "myAppointment";
    }

    @Override
    public String getHelp() {
        return null;
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    };
}
