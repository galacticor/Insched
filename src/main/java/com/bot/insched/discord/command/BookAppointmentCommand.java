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
        return "!bookAppointment <ID appointment> <judul> <deskripsi>\n" +
                "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2 Demo_AP tutorial_2";
    }

    public String createBooking(String[] args, PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String appointmentId = args[0];
        String title = args[1];
        String description = args[2];

        return bookingAppointmentService.createBooking(title, description, appointmentId, userId);
    }

    private void sendMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
