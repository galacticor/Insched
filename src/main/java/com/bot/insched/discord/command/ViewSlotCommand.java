package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.BookingAppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.List;

public class ViewSlotCommand implements Command{

    private BookingAppointmentService bookingAppointmentService;

    private MessageSender sender = MessageSender.getInstance();

    public ViewSlotCommand(BookingAppointmentService bookingAppointmentService) {
        this.bookingAppointmentService = bookingAppointmentService;
    }


    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        try {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendPrivateMessage(getHelp(), event);
            } else {
                List<Event> eventList = viewHandler(args, event);
                InschedEmbed response = embedHandler(eventList, args[0]);
                sender.sendPrivateMessage(response.build(), event);
            }
        } catch (IndexOutOfBoundsException e) {
            sender.sendPrivateMessage(
                    "Masukkan argumen yang sesuai!\n" +
                            "Penggunaan: !viewSlot <host-token>\n" +
                            "Help: !viewSlot help", event);
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "viewSlot";
    }

    @Override
    public String getHelp() {
        return "Menampilkan semua slot milik Host\n" +
                "Penggunaan: !viewSlot <host-token>\n" +
                "Contoh: !viewSlot f2da393a-7ef2-4fe9-979e-ea3d76adc7ea";
    }

    public List<Event> viewHandler(String[] args, PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];

        return bookingAppointmentService.viewHostBookingSlots(userId, token);
    }

    public InschedEmbed embedHandler(List<Event> eventList, String appointmentToken) throws Exception {
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Appointment List");
        embed.setDescription("Token: " + appointmentToken);

        for (Event event : eventList) {
                String desc = event.getDescription();
                String eventToken = "Token: " + event.getIdEvent().toString() + "\n";
                String time = event.getWaktu() + "\n";
                String bookingStatus = event.getStatusBooking();
                embed.addField(desc, time + eventToken + bookingStatus, false);
        }
        return embed;
    }

}
