package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.BookingAppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class DeleteBookAppointmentCommand implements Command{

    private BookingAppointmentService bookingAppointmentService;

    private MessageSender sender = MessageSender.getInstance();

    public DeleteBookAppointmentCommand(BookingAppointmentService bookingAppointmentService) {
        this.bookingAppointmentService = bookingAppointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {

        try {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendPrivateMessage(getHelp(), event);
            } else {
                String result = deletionHandler(args, event);
                InschedEmbed response = embedHandler(result);
                sender.sendPrivateMessage(response.build(), event);
            }
        } catch (IndexOutOfBoundsException e) {
            sender.sendPrivateMessage(
                    "Masukkan argumen yang sesuai!\n" +
                            "Penggunaan: !unbookAppointment <token_event>\n" +
                            "Help: !unbookAppointment help", event);
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }

    }

    @Override
    public String getCommand() {
        return "unbookAppointment";
    }

    @Override
    public String getHelp() {
        return "Digunakan untuk menghapus booking pada slot event dalam sebuah appointment.\n" +
                "Penggunaan: !unbookAppointment <token_event>\n" +
                "Contoh: !unbookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    }

    public String deletionHandler(String[] args, PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];

        return bookingAppointmentService.deleteBooking(userId, token);
    }

    public InschedEmbed embedHandler(String result) {

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Unbook Appointment");
        embed.setDescription(result);

        return embed;
    }
}
