package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.BookingAppointmentService;
import java.time.LocalDate;
import java.util.List;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class ViewSlotCommand implements Command {

    private BookingAppointmentService bookingAppointmentService;

    private MessageSender sender = MessageSender.getInstance();

    public ViewSlotCommand(BookingAppointmentService bookingAppointmentService) {
        this.bookingAppointmentService = bookingAppointmentService;
    }


    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        try {
            List<Event> eventList = viewHandler(args, event);
            InschedEmbed response = embedHandler(eventList, args[0]);
            sender.sendPrivateMessage(response.build(), event);
        } catch (IndexOutOfBoundsException e) {
            sender.sendPrivateMessage(
                    "Masukkan argumen yang sesuai!\n"
                            + "Penggunaan: !viewSlot <host-token>\n"
                            + "Help: !viewSlot help", event);
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
        return "Menampilkan semua slot milik Host\n"
                + "Penggunaan: !viewSlot <host-token>\n"
                + "Contoh: !viewSlot f2da393a-7ef2-4fe9-979e-ea3d76adc7ea";
    }

    public List<Event> viewHandler(String[] args, PrivateMessageReceivedEvent event)
            throws Exception {
        int argsLength = args.length;
        if (argsLength != 1) {
            throw new IndexOutOfBoundsException();
        }

        String userId = event.getAuthor().getId();
        String token = args[0];

        return bookingAppointmentService.viewHostBookingSlots(userId, token);
    }

    public InschedEmbed embedHandler(List<Event> eventList, String appointmentToken) {
        LocalDate now = LocalDate.now();
        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Appointment List");
        embed.setDescription("Token: " + appointmentToken);

        for (Event event : eventList) {
            if (!event.getTanggal().isBefore(now)) {
                String desc = event.getDescription();
                String date = "Tanggal: " + event.getTanggal().toString() + "\n";
                String eventToken = "Token: " + event.getIdEvent().toString() + "\n";
                String time = event.getWaktu() + "\n";
                String bookingStatus = event.getStatusBooking();
                embed.addField(desc, date + time + eventToken + bookingStatus, false);

            }
        }
        return embed;
    }

}
