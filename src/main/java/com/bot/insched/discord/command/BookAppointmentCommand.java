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

        try {
            String result = creationHandler(args, event);
            InschedEmbed response = embedHandler(result);
            sender.sendPrivateMessage(response.build(), event);
        } catch (IndexOutOfBoundsException e) {
            sender.sendPrivateMessage(
                    "Masukkan argumen yang sesuai!\n"
                            + "Penggunaan: !bookAppointment <token_event>\n"
                            + "Help: !bookAppointment help", event);
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
        return "Digunakan untuk membuat booking pada slot event dalam sebuah appointment.\n"
                + "Penggunaan: !bookAppointment <token_event>\n"
                + "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    }

    public String creationHandler(String[] args,
                                  PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];

        return bookingAppointmentService.createBooking(userId, token);
    }

    public InschedEmbed embedHandler(String result) {

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Book Appointment");
        embed.setDescription(result);

        return embed;
    }
}
