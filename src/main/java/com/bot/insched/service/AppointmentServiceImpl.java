package com.bot.insched.service;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.bot.insched.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DiscordUserService discordUserService;

    @Autowired
    EventService eventService;

    @Autowired
    EventRepository eventRepository;

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
    public List<Event> getAllAppointment(String idDiscord) throws Exception {
        DiscordUser user = discordUserService.findByUserId(idDiscord);
        if (user == null) {
            throw new NotLoggedInException();
        }
        Appointment app = appointmentRepository.findAppointmentByOwner(user);
        return app.getListEvent();
    }

    @Override
    public String editSlot(String token, String jamBaru, int durasiBaru, String judulBaru,
                                  String idDiscord) throws Exception {
        DiscordUser user = discordUserService.findByUserId(idDiscord);
        if (user == null) {
            throw new NotLoggedInException();
        }

        Event event = eventRepository.findByIdEvent(UUID.fromString(token));

        if (event == null) {
            return "Tidak ada slot dengan kode tersebut!";
        } else if (event.getListAttendee().size() == 0) {
            String tanggal = event.getStartTime().toLocalDate().toString();
            LocalDateTime newStartTime = LocalDateTime.parse(tanggal + "T" + jamBaru + ":00");

            event.setStartTime(newStartTime);
            event.setEndTime(newStartTime.plusMinutes(durasiBaru));
            return "Appointment berhasil di-update!";
        }

        return "Jangan melanggar ketentuan yang sudah diberikan!";
    }

    @Override
    public String deleteSlot(String token, String idDiscord) throws Exception {
        DiscordUser user = discordUserService.findByUserId(idDiscord);
        if (user == null) {
            throw new NotLoggedInException();
        }

        Event event = eventRepository.findByIdEvent(UUID.fromString(token));
        if (event == null) {
            throw new SlotUnavailableException("Tidak ada slot pada keterangan waktu seperti itu!");
        } else if (event.getListAttendee().size() == 0){
            eventRepository.deleteByIdEvent(UUID.fromString(token));
            return "Slot berhasil dihapus!";
        }

        return "Slot sudah dibooking!";
    }
}