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
        String desc = args[0];
        String startDate = args[1];
        String endDate = args[2];
        String discordId = event.getAuthor().getId();
        String response = appointmentService.createAppointment(desc, startDate, endDate, discordId);

        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response);
        });

    }

    @Override
    public String getCommand() {
        return "createAppointment";
    }

}
