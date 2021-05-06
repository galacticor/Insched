package com.bot.insched.discord.command;

import com.bot.insched.model.Appointment;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateAppointmentSlot implements Command{

    private AppointmentService appointmentService;
    private DiscordUserService discordUserService;

    public CreateAppointmentSlot(AppointmentService appointmentService,
                                 DiscordUserService discordUserService) {
        this.appointmentService = appointmentService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        if (args[0].equalsIgnoreCase("help")) {
            sendPrivateMessage(getHelp(), event);
        } else {
            try{
                String res = handleCreation(args);
                sendPrivateMessage(res, event);
            } catch (IndexOutOfBoundsException e) {
                sendPrivateMessage("Masukan argumen yang sesuai!", event);
            } catch (Exception e) {
                sendPrivateMessage(e.getMessage(), event);
            }
        }
    }

    @Override
    public String getCommand() {
        return "createSlot";
    }

    @Override
    public String getHelp() {
        return "!createSlot <token_appointment> <tanggal> <jam_mulai> <durasi (menit)> <kapasitas_attendee>\n" +
                "Contoh: !createSlot 1234-rsrf0-ir9-3 2021-05-20 15:30 30 2";
    }

    public String handleCreation(String[] args) throws Exception {
        String token = args[0];
        LocalDate tanggal = LocalDate.parse(args[1]);
        LocalDateTime jam = LocalDateTime.parse(args[1] + "T" + args[2] + ":00");
        int durasi = Integer.parseInt(args[3]);
        int kapasitas = Integer.parseInt(args[4]);

        Appointment appointment = appointmentService.findAppointmentById(token);
        if (appointment == null) {
            throw new Exception("Tidak ada appointment dengan token tersebut");
        } else if (tanggal.isBefore(appointment.getStartDate()) || tanggal.isAfter(appointment.getEndDate())) {
            throw new Exception("Masukan tanggal yang sesuai!");
        }

        return appointmentService.createSlot(jam.toString(), durasi, kapasitas, appointment);
    }

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
