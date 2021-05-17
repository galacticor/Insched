package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.AppointmentService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class EditSlotCommand implements Command{

    private MessageSender sender = MessageSender.getInstance();

    private AppointmentService appointmentService;


    public EditSlotCommand(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    };

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        try {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendPrivateMessage(getHelp(), event);
            } else {
                String response = handleUpdate(args, event);
                InschedEmbed embed = new InschedEmbed();
                embed.setTitle("Update Appointment Status");
                embed.setDescription(response);
                sender.sendPrivateMessage(embed.build(), event);
            }
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "editSlot";
    }

    @Override
    public String getHelp() {
        String help = "Digunakan untuk update judul dan jam appointment.\n" +
            "Hanya dapat mengupdate appointment yang belum dibooking.\n\n" +
            "Penggunaan: !editSlot token_slot jam_baru durasi_baru judul_baru\n\n" +
            "Contoh: !editSlot 94a56007-cbe4-47a0-aa54-2c0689c7e19c 17:00 30 Sprint_report\n\n" +
            "Jika kamu tidak ingin mengupdate field tertentu, isikan dengan -";
        return help;
    }

    public String handleUpdate(String[] args, PrivateMessageReceivedEvent event) throws Exception{
        String token = args[0];
        String jamBaru = args[1];
        int durasiBaru = Integer.parseInt(args[2]);
        String judulBaru = args[3];
        String idDiscord = event.getAuthor().getId();
        return appointmentService.editSlot(token, jamBaru, durasiBaru, judulBaru, idDiscord);
    }
}
