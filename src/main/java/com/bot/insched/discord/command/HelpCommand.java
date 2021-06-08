package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelpCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Berikut adalah daftar Command Insched Discord Bot :");
        embed.addField("`!login`",  "dapat digunakan untuk login ke Google Calendar anda", false);
        embed.addField("`!logout`",  "dapat digunakan untuk login ke Google Calendar anda", false);
        embed.addField("`!hello`",  "dapat digunakan untuk memastikan akun yang terdaftar "
                + "di Google Calendar", false);
        embed.addField("`!createEvent`",  "dapat digunakan untuk membuat event pada "
                + "Google Calendar", false);
        embed.addField("`!deleteEvent`",  "dapat digunakan untuk menghapus event pada "
                + "Google Calendar", false);
        embed.addField("`!updateEvent`",  "dapat digunakan untuk mengupdate event pada "
                + "Google Calendar", false);
        embed.addField("`!getEvent`",  "dapat digunakan untuk mendapatkan event pada "
                + "Google Calendar", false);
        embed.addField("`!createAppointment`",  "dapat digunakan untuk membuat, "
                + "mengubah, dan menghapus appointment pada Google Calendar anda", false);
        embed.addField("`!createSlot`",  "dapat digunakan untuk membuat slot event pada "
                + "Google Calendar", false);
        embed.addField("`!editSlot`",  "dapat digunakan untuk mengedit slot event pada "
                + "Google Calendar", false);
        embed.addField("`!bookAppointment`",  "dapat digunakan untuk book "
                + "appointment", false);
        embed.addField("`!myAppointmentList`",  "dapat digunakan untuk melihat daftar "
                + "appointment", false);
        embed.addField("`!myToken`",  "dapat digunakan untuk melihat token ", false);
        embed.addField("`!viewSlot`",  "dapat digunakan untuk melihat slot appointment", false);
        embed.addField("`!showCalendar`",  "dapat digunakan untuk melihat Google Calendar anda",
                false);

        sender.sendPrivateMessage(embed.build(),event);
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelp() {
        return null;
    }
}