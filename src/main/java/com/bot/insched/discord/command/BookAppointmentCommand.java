package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.service.BookingAppointmentService;
import com.bot.insched.service.EventService;
import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class BookAppointmentCommand implements Command {

    private BookingAppointmentService bookingAppointmentService;

    private EventService eventService;

    private GoogleService googleService;

    private MessageSender sender = MessageSender.getInstance();

    public BookAppointmentCommand(BookingAppointmentService bookingAppointmentService,
                                  EventService eventService,
                                  GoogleService googleService) {
        this.bookingAppointmentService = bookingAppointmentService;
        this.eventService = eventService;
        this.googleService = googleService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        if (args.length > 0 && args[0].equals("help")) {
            sender.sendPrivateMessage(getHelp(), event);
            return;
        }

        try {
            String result = creationHandler(args, event);
            InschedEmbed response = embedHandler(result);

            String hostIdDiscord = findHostUser(args[0]).getIdDiscord();
            InschedEmbed notifMessage = handleNotifMessage(args[0]);

            sender.sendPrivateMessage(response.build(), event);
            sender.sendPrivateNotificationById(notifMessage.build(), hostIdDiscord);

        } catch (IndexOutOfBoundsException e) {
            sender.sendPrivateMessage(
                "Masukkan argumen yang sesuai!\n"
                    + "Penggunaan: !bookAppointment <token_event>\n"
                    + "Help: !bookAppointment help", event);
        } catch (Exception e) {
            sender.sendPrivateMessage(e.getMessage(), event);
        }

    }

    @Override
    public String getCommand() {
        return "bookAppointment";
    }

    @Override
    public String getHelp() {
        return "Digunakan untuk membuat booking pada slot event dalam sebuah appointment.\n"
            + "Penggunaan: !bookAppointment <token_event> <email_kamu>\n"
            + "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2 joe@gmail.com\n"
            + "Atau jika kamu sudah login, maka\n"
            + "Penggunaan: !bookAppointment <token_event>\n"
            + "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    }

    public String creationHandler(String[] args,
                                  PrivateMessageReceivedEvent event) throws Exception {

        String userId = event.getAuthor().getId();
        String token = args[0];

        int argsLength = args.length;
        if (argsLength == 0) {
            throw new IndexOutOfBoundsException();
        }

        String email;

        if (argsLength == 2) {
            if (!args[1].contains("@")) {
                throw new Exception("Masukkan email kamu dengan benar.");
            }
            email = args[1];
        } else {
            email = googleService.getUserInfo(userId);
        }

        return bookingAppointmentService.createBooking(userId, token, email);
    }

    public InschedEmbed embedHandler(String result) {

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Book Appointment");
        embed.setDescription(result);

        return embed;
    }

    public DiscordUser findHostUser(String eventToken) {
        DiscordUser user = eventService.findById(eventToken).getAppointment().getOwner();
        return user;
    }

    public InschedEmbed handleNotifMessage(String eventToken) {
        Event e = eventService.findById(eventToken);
        String res = "Slot pada ";
        res += e.getTanggal() + " ";
        res += e.getWaktu() + " ";
        res += "telah dibooking!";

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Notification");
        embed.addField("", res, false);

        return embed;
    }
}
