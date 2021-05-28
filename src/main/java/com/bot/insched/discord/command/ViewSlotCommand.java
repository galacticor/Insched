package com.bot.insched.discord.command;

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

    }

    @Override
    public String getCommand() {
        return "viewSlot";
    }

    @Override
    public String getHelp() {
        return "Menampilkan semua slot milik Host\n" +
                "!viewSlot <host-token>\n" +
                "Contoh: !viewSlot f2da393a-7ef2-4fe9-979e-ea3d76adc7ea";
    }

    public List<Event> viewHandler(String[] args, PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];

        return bookingAppointmentService.viewHostBookingSlots(userId, token);
    }

}
