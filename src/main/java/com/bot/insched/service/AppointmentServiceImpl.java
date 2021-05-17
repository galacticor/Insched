package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DiscordUserService discordUserService;

    @Autowired
    EventService eventService;

    @Override
    public Appointment save(Appointment app) {
        return appointmentRepository.save(app);
    }

    @Override
    public String getUserToken(DiscordUser discordUser) {
        if (discordUser.getAppointment() == null) {
            Appointment app = new Appointment();
            app.setOwner(discordUser);
            discordUser.setAppointment(app);
            appointmentRepository.save(app);
            discordUserService.save(discordUser);
        }
        return discordUser.getAppointment().getIdAppointment().toString();
    }

    @Override
    public String createSlot(String deskripsi, String waktu, int durasi, int kapasitas,
                             String idDiscord) throws Exception {

        LocalDateTime mulai = LocalDateTime.parse(waktu);
        DiscordUser user = discordUserService.findByUserId(idDiscord);
        if (user == null) {
            throw new NotLoggedInException();
        }
        List<Event> userAppointment =  user.getAppointment().getListEvent();

        for (Event e : userAppointment) {
            if (e.getStartTime().isEqual(mulai)) {
                throw new SlotUnavailableException(
                    "Jam sudah memiliki slot! Silahkan pilih jam/tanggal lain");
            }
        }

        Event event = new Event(mulai.toString(), durasi, kapasitas, deskripsi);
        DiscordUser discordUser = discordUserService.findByUserId(idDiscord);
        Appointment app = appointmentRepository.findAppointmentByOwner(discordUser);

        app.getListEvent().add(event);
        event.setAppointment(app);
        eventService.save(event);
        save(app);

        return "Slot berhasil dibuat!";
    }

    @Override
    public List<Event> getAllAppointment(String idDiscord) {
        DiscordUser user = discordUserService.findByUserId(idDiscord);
        Appointment app = appointmentRepository.findAppointmentByOwner(user);
        return app.getListEvent();
    }

}