package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelpCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String res = getHelp();
        sender.sendPrivateMessage(res, event);
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelp() {
        String help = "Terdapat 5 fitur pada chat bot ini lakukan pada Google Calendar anda:\n"
                + "1. Create Event (!createEvent): dapat digunakan untuk membuat, mengubah, "
                + "dan menghapus event pada Google Calendar \n"
                + "2. Create Appointment (!createAppointment): dapat digunakan untuk membuat, "
                + "mengubah, dan menghapus appointment pada Google Calendar anda \n"
                + "3. Book Appointment (!bookAppointment): dapat digunakan untuk book "
                + "appointment \n"
                + "4. Show Calendar (!showCalendar): dapat menunjukkan jadwal pada Calendar anda \n"
                + "5. Authentication / Login (!login): untuk melakukan login pada Google Calendar";
        return help;
    }
}
