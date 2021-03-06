package com.bot.insched.service;

import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.repository.EventRepository;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private GoogleApiManager manager;

    @Autowired
    EventRepository eventRepository;

    public Event getEventService(String discordId, String eventId) {
        Calendar calendar = manager.getCalendarService(discordId);
        try {
            Event event = calendar.events().get("primary", eventId).execute();
            return event;
        } catch (Exception e) {
            log.error("------ Error when getEventService: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String getEventIdService(String discordId, String eventId) {
        Calendar calendar = manager.getCalendarService(discordId);
        try {
            Event event = getEventService(discordId, eventId);
            String judul = event.getSummary();
            String link = event.getHtmlLink();
            String id = event.getId();
            String awal = event.getStart().getDateTime().toString();
            String tglAwal = awal.split("T")[0];
            String jamAwal = awal.split("T")[1];
            String selesai = event.getEnd().getDateTime().toString();
            String tglSelesai = selesai.split("T")[0];
            String jamSelesai = selesai.split("T")[1];
            String deskripsi = event.getDescription();
            String ret = String.format("Event yang anda cari berhasil ditemukan \n"
                            + "Berikut link event-nya: [LINK](%s) \n"
                            + ":id: %s \n"
                            + "judul: %s \n"
                            + "\n"
                            + ":clock: Mulai pada  : %s jam %s \n"
                            + ":clock: Selesai pada: %s jam %s \n"
                            + "\n"
                            + "Deskripsi Event anda adalah %s", link, id, judul, tglAwal, jamAwal,
                    tglSelesai, jamSelesai, deskripsi);
            return ret;
        } catch (Exception e) {
            log.error("------ Error when getEventIdService: {}", e.getMessage());
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public Calendar getCalendarbyId(String discordId) {
        return manager.getCalendarService(discordId);
    }

    @Override
    public String createEventService(String discordId, Event event) {
        Calendar calendar = getCalendarbyId(discordId);
        if (calendar == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            Event newEvent = calendar.events().insert("primary", event).execute();
            String judul = newEvent.getSummary();
            String link = newEvent.getHtmlLink();
            String id = newEvent.getId();
            String awal = newEvent.getStart().getDateTime().toString();
            String tglAwal = awal.split("T")[0];
            String jamAwal = awal.split("T")[1];
            String selesai = newEvent.getEnd().getDateTime().toString();
            String tglSelesai = selesai.split("T")[0];
            String jamSelesai = selesai.split("T")[1];
            String deskripsi = newEvent.getDescription();
            String ret = String.format("Event anda berhasil dibuat \n"
                            + "Berikut link event anda: [LINK](%s) \n"
                            + ":id: %s \n"
                            + "judul: %s \n"
                            + "\n"
                            + ":clock: Mulai pada  : %s jam %s \n"
                            + ":clock: Selesai pada: %s jam %s \n"
                            + "\n"
                            + "Deskripsi Event anda adalah %s", link, id, judul, tglAwal, jamAwal,
                    tglSelesai, jamSelesai, deskripsi);
            return ret;
        } catch (Exception e) {
            log.error("------ Error when createEventService: {}", e.getMessage());
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public String deleteEventService(String discordId, String eventId) {
        Calendar calendar = getCalendarbyId(discordId);
        if (calendar == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            calendar.events().delete("primary", eventId).execute();
            return "Event berhasil terhapus!";
        } catch (Exception e) {
            log.error("------ Error when deleteEventService: {}", e.getMessage());
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public String updateEventService(String discordId, String eventId,
                                     String jenis, String newData) {
        Calendar calendar = getCalendarbyId(discordId);
        if (calendar == null) {
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            Event event = getEventService(discordId, eventId);
            if (jenis.equalsIgnoreCase("deskripsi")) {
                event.setDescription(newData);
            } else if (jenis.equalsIgnoreCase("mulai")) {
                DateTime dateTime = new DateTime(newData);
                event.setStart(new EventDateTime().setDateTime(dateTime));
            } else if (jenis.equalsIgnoreCase("selesai")) {
                DateTime dateTime = new DateTime(newData);
                event.setEnd(new EventDateTime().setDateTime(dateTime));
            } else if (jenis.equalsIgnoreCase("summary")) {
                event.setSummary(newData);
            }
            Event newEvent = calendar.events().update("primary", eventId, event).execute();
            String judul = newEvent.getSummary();
            String link = newEvent.getHtmlLink();
            String id = newEvent.getId();
            String awal = newEvent.getStart().getDateTime().toString();
            String tglAwal = awal.split("T")[0];
            String jamAwal = awal.split("T")[1];
            String selesai = newEvent.getEnd().getDateTime().toString();
            String tglSelesai = selesai.split("T")[0];
            String jamSelesai = selesai.split("T")[1];
            String deskripsi = newEvent.getDescription();
            String ret = String.format("Event anda berhasil di-update \n"
                            + "Berikut link event anda: [LINK](%s) \n"
                            + ":id: %s \n"
                            + "judul: %s \n"
                            + "\n"
                            + ":clock: Mulai pada  : %s jam %s \n"
                            + ":clock: Selesai pada: %s jam %s \n"
                            + "\n"
                            + "Deskripsi Event anda adalah %s", link, id, judul, tglAwal, jamAwal,
                    tglSelesai, jamSelesai, deskripsi);
            return ret;

        } catch (Exception e) {
            log.error("------ Error when updateEventService: {}", e.getMessage());
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    // create appointment -> buat event di model
    // booking appointment -> panggil updateSlotEventService untuk:
    // apabila attendee pertama: panggil updateSlotEvent yang akan memanggil createSlotEvent
    // untuk membuat event di google calendar sesuai event di model dan menambah attendee pertama
    // apabila attendee kedua dan seterusnya: panggil updateSlotEvent untuk menambah attendee
    @Override
    public Event updateSlotEventService(String discordId,
                                        String newData, com.bot.insched.model.Event eventModel) {
        Calendar calendar = getCalendarbyId(discordId);
        if (calendar == null) {
            return null;
        }
        try {
            // event di google calendar
            String eventId = eventModel.getIdGoogleEvent();
            Event eventCalendar = getEventService(discordId, eventId);

            // cek eventnya sudah ada atau belum di google calendar
            log.warn("######### : CalendarEvent {}", eventCalendar);
            if (eventCalendar == null) {
                eventCalendar = createSlotEventService(discordId, eventId, eventModel);
                eventId = eventCalendar.getId();
            }
            List<EventAttendee> attendeeList = eventCalendar.getAttendees();
            if (attendeeList == null) {
                attendeeList = new ArrayList<EventAttendee>();
            }

            // cek kapasitas
            if (eventModel.getCapacity() < attendeeList.size()) {
                return null;
            }
            EventAttendee attendee = new EventAttendee();
            attendee.setEmail(newData);
            attendeeList.add(attendee);
            eventCalendar.setAttendees(attendeeList);
            Event newEvent = calendar.events().update("primary", eventId, eventCalendar).execute();
            return newEvent;

        } catch (Exception e) {
            log.error("------ Error when updateSlotEventService: {}", e.getMessage());
            return null;
        }
    }

    // Dipanggil saat saat attendee pertama pada slot
    @Override
    public Event createSlotEventService(String discordId, String eventId,
                                        com.bot.insched.model.Event eventModel) {
        Calendar calendar = getCalendarbyId(discordId);
        if (calendar == null) {
            return null;
        }
        try {
            // event di google calendar
            Event eventCalendar = new Event();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_DATE_TIME;

            DateTime dateTime = new DateTime(eventModel.getStartTime().minusHours(7)
                                .format(dateFormatter));
            eventCalendar.setStart(new EventDateTime().setDateTime(dateTime));
            dateTime = new DateTime(eventModel.getEndTime().minusHours(7).format(dateFormatter));
            eventCalendar.setEnd(new EventDateTime().setDateTime(dateTime));
            eventCalendar.setSummary(eventModel.getDescription());
            eventCalendar = calendar.events().insert("primary", eventCalendar).execute();
            eventModel.setIdGoogleEvent(eventCalendar.getId());
            eventRepository.save(eventModel);
            return eventCalendar;

        } catch (Exception e) {
            log.error("------ Error when createSlotEventService: {}", e.getMessage());
            return null;
        }
    }


    @Override
    public com.bot.insched.model.Event save(com.bot.insched.model.Event event) {
        return eventRepository.save(event);
    }

    public com.bot.insched.model.Event findById(String id) {
        return eventRepository.findByIdEvent(UUID.fromString(id));
    }

    public void deleteEventFromRepo(UUID token) {
        eventRepository.deleteByIdEvent(token);
    }
}
