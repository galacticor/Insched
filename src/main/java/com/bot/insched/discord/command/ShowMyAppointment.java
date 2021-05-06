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

        if (args.length != 0 && args[0].equalsIgnoreCase("help")) {
            sendPrivateMessage("Fitur yang digunakan untuk melihat appointment mu.", event);
        } else {
            String idDiscord = event.getAuthor().getId();
            List<Appointment> listAppointment = appointmentService.getAllUserAppointment(idDiscord);
            InschedEmbed embed = embedHandler(listAppointment);

            event.getAuthor().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(embed.build()).queue();
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

    private InschedEmbed embedHandler(List<Appointment> appointmentList) {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Your Appointment List");
        for (Appointment elem: appointmentList){
            String desc = elem.getDescription();
            String field = fieldHandler(elem);
            embed.addField(desc, field, false);
        }
        return embed;
    }

    private String fieldHandler(Appointment app) {
        String startDate = String.format("Start date: %s \n", app.getStartDate().toString());
        String endDate = String.format("End date: %s \n", app.getEndDate().toString());
        String token = String.format("Token: %s \n", app.getIdAppointment());
        return startDate + endDate + token;
    }
}
