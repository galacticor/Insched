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
        return "updateAppointment";
    }

    @Override
    public String getHelp() {
        String help = "Digunakan untuk update judul dan jam appointment.\n" +
            "Hanya dapat mengupdate appointment yang belum dibooking.\n" +
            "Penggunaan: !updateAppointment tanggal jam_lama jam_baru durasi_baru judul_baru\n" +
            "Contoh: !updateAppointment 2021-08-03 15:30 17:00 30 Sprint_report\n" +
            "Jika kamu tidak ingin mengupdate field tertentu, isikan dengan -";
        return help;
    }

    public String handleUpdate(String[] args, PrivateMessageReceivedEvent event) throws Exception{
        String tanggal = args[0];
        String jamLama = args[1];
        String jamBaru = args[2];
        int durasiBaru = Integer.parseInt(args[3]);
        String judulBaru = args[3];
        String idDiscord = event.getAuthor().getId();

        return appointmentService.editSlot(tanggal, jamLama, jamBaru, durasiBaru, judulBaru, idDiscord);
    }
}
