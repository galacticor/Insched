package com.bot.insched.discord.command;

import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.EventService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AppointmentDetailsCommand implements Command {


    private MessageSender sender = MessageSender.getInstance();
    private EventService eventService;

    public AppointmentDetailsCommand(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        if (args.length > 0) {
            try {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendPrivateMessage(getHelp(), event);
                } else {
                    InschedEmbed response = findAppointment(args[0]);
                    sender.sendPrivateMessage(response.build(), event);
                }
            } catch (IllegalArgumentException e) {
                // Catching invalid UUID format
                sender.sendPrivateMessage("Tidak ada slot appointment dengan token tersebut!",
                    event);
            } catch (Exception e) {
                sender.sendPrivateMessage(e.toString(), event);
            }
        }

    }

    @Override
    public String getCommand() {
        return "appointmentDetails";
    }

    @Override
    public String getHelp() {
        return "Penggunaan: !appointmentDetails token_event\n"
            + "Contoh: !appointmentDetails 6f99e80c-e8c6-48ee-b0c1-e349a9ba9ddb";
    }

    public InschedEmbed findAppointment(String token) throws Exception {
        Event event = eventService.findById(token);

        if (event == null) {
            throw new SlotUnavailableException("Tidak ada slot appointment dengan token tersebut!");
        }

        InschedEmbed embed = new InschedEmbed();
        String isAvailable = event.isAvailable() ? "Masih available" : "Slot sudah penuh";

        embed.setTitle("Appointment Details");
        embed.addField("Deskripsi:", event.getDescription(), false);
        embed.addField("Tanggal:", event.getTanggal().toString(), false);
        embed.addField("Waktu:", event.getWaktu(), false);
        embed.addField("Status booking: ", event.getStatusBooking(), false);
        embed.addField("Availability:", isAvailable, false);
        embed.addField("Token: ", event.getIdEvent().toString(), false);

        return embed;
    }
}
