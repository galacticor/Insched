package com.bot.insched.discord.command;

import com.bot.insched.service.BookingAppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class BookAppointmentCommand implements Command {

    private BookingAppointmentService bookingAppointmentService;

    public BookAppointmentCommand(BookingAppointmentService bookingAppointmentService) {
        this.bookingAppointmentService = bookingAppointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Selamat Datang di fitur BookAppointment").queue();
        });

        try {
            String response = createBooking(args, event);
            sendMessage(response,event);
        } catch (IndexOutOfBoundsException e) {
            sendMessage("Masukan argumen yang sesuai!", event);
        } catch (Exception e) {
            sendMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "bookAppointment";
    }

    @Override
    public String getHelp() {
        return "!bookAppointment <token_owner> <tanggal> <jam>\n" +
                "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2 2021-08-03 15:30";
    }

    public String createBooking(String[] args, PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];
        String datetime = args[1] + "T" + args[1] + ":00";;

        return bookingAppointmentService.createBooking(userId, token, datetime);
    }

    private void sendMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
