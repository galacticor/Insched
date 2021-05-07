package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String res = getHelp();
        sendPrivateMessage(res,event);
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
                + "4. Show Calendar (!showCalendar): dapat menunjukkan jadwal pada Calendar anda"
                + "5. Authentication (!auth): untuk melakukan login pada Google Calendar";
        return help;
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
