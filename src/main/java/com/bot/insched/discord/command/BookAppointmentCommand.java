package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.BookingAppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class BookAppointmentCommand implements Command {

    private BookingAppointmentService bookingAppointmentService;

    private MessageSender sender = MessageSender.getInstance();

    public BookAppointmentCommand(BookingAppointmentService bookingAppointmentService) {
        this.bookingAppointmentService = bookingAppointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Book Appointment");
        embed.setDescription("Selamat Datang di fitur Book Appointment");

        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(embed.build()).queue();
        });

        try {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendPrivateMessage(getHelp(), event);
            } else {
                String response = creationHandler(args, event);
                sender.sendPrivateMessage(response, event);
            }
        } catch (IndexOutOfBoundsException e) {
            sender.sendPrivateMessage(
                    "Masukkan argumen yang sesuai!\n" +
                            "Penggunaan: !bookAppointment <token_event>\n" +
                            "Help: !bookAppointment help", event);
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "bookAppointment";
    }

    @Override
    public String getHelp() {
        return "Digunakan untuk membuat booking pada slot event dalam sebuah appointment.\n" +
               "Penggunaan: !bookAppointment <token_event>\n" +
               "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    }

    public String creationHandler(String[] args, PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];

        return bookingAppointmentService.createBooking(userId, token);
    }
}
