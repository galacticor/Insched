package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.AppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class DeleteSlotCommand implements Command {

    private MessageSender sender = MessageSender.getInstance();

    private AppointmentService appointmentService;

    public DeleteSlotCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        try {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendPrivateMessage(getHelp(), event);
            } else {
                String idDiscord = event.getAuthor().getId();
                String response = handleDelete(args, idDiscord);
                sender.sendPrivateMessage(response, event);
            }
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "deleteAppointment";
    }

    @Override
    public String getHelp() {
        return "Digunakan untuk menghapus appointment yang belum dibooking.\n" +
            "Penggunaan: !deleteAppointment tanggal jam_mulai\n" +
            "Contoh: !deleteAppointment 2021-06-07 15:30";
    }

    public String handleDelete(String[] args, String idDiscord) throws Exception {
        String tanggal = args[0];
        String jam = args[1];
        String response = appointmentService.deleteSlot(tanggal, jam, idDiscord);
        return response;
    }
}
