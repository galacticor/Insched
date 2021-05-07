package com.bot.insched.discord.command;

import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class CreateSlotCommand implements Command{

    private AppointmentService appointmentService;
    private DiscordUserService discordUserService;

    public CreateSlotCommand(AppointmentService appointmentService,
                             DiscordUserService discordUserService) {
        this.appointmentService = appointmentService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {

        try {
            String response = creationHandler(args, event);
            sendMessage(response, event);
        } catch (IndexOutOfBoundsException e) {
            sendMessage("Masukan argumen yang sesuai!", event);
        } catch (Exception e) {
            sendMessage(e.getMessage(), event);
        }
    }

    @Override
    public String getCommand() {
        return "createSlot";
    }

    @Override
    public String getHelp() {
        return "!createSlot tanggal jam_mulai durasi(menit) kapasitas deskripsi" +
                "Contoh: !createSlot 2022-02-03 15:30 30 2 meeting_startup";
    }

    public String creationHandler(String[] args, PrivateMessageReceivedEvent event) throws Exception {
        String waktu = args[0] + "T" + args[1] + ":00"; // ex: 2021-05-03T15:30:00
        int durasi = Integer.parseInt(args[2]);
        int kapasitas = Integer.parseInt(args[3]);
        String deskripsi = args[4];
        String idUser = event.getAuthor().getId();

        return appointmentService.createSlot(deskripsi,waktu,durasi, kapasitas, idUser);
    }

    private void sendMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
