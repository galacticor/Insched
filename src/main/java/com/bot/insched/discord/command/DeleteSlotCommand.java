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
        return "deleteSlot";
    }

    @Override
    public String getHelp() {
        return "Digunakan untuk menghapus appointment yang belum dibooking.\n"
            + "Penggunaan: !deleteSlot slot_token\n"
            + "Contoh: !deleteSlot 12f8ca5e-8888-45b8-881b-a58d349e5269";
    }

    public String handleDelete(String[] args, String idDiscord) throws Exception {
        String token = args[0];
        String response = appointmentService.deleteSlot(token, idDiscord);
        return response;
    }
}
