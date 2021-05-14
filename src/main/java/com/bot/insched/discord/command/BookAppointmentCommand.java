package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class BookAppointmentCommand implements Command {

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Book Appointment");
        embed.setDescription("Selamat Datang di fitur Book Appointment");

        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getCommand() {
        return "bookAppointment";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
