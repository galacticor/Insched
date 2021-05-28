
package com.bot.insched.service;

import com.bot.insched.google.GoogleApiManager;
import com.bot.insched.repository.EventRepository;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getEventIdService(String discordId, String eventId) {
        Calendar calendar = manager.getCalendarService(discordId);
        try {
            Event event = getEventService(discordId, eventId);
            String link = event.getHtmlLink();
            String id = event.getId();
            String tglAwal = event.getStart().getDateTime().toString();
            String tglSelesai = event.getEnd().getDateTime().toString();
            String deskripsi = event.getDescription();
            String ret = String.format("Event yang anda cari berhasil ditemukan \n"
                    + "Berikut link event yang anda cari: [LINK](%s) \n"
                    + ":id: %s \n"
                    + "\n"
                    + ":clock: Mulai pada  : %s \n"
                    + ":clock: Selesai pada: %s \n"
                    + "\n"
                    + "Deskripsi Event anda adalah %s", link, id, tglAwal, tglSelesai, deskripsi);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
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
            String link = newEvent.getHtmlLink();
            String id = newEvent.getId();
            String tglAwal = newEvent.getStart().getDateTime().toString();
            String tglSelesai = newEvent.getEnd().getDateTime().toString();
            String deskripsi = newEvent.getDescription();
            String ret = String.format("Event yang anda cari berhasil ditemukan \n"
                    + "Berikut link event yang anda cari: [LINK](%s) \n"
                    + ":id: %s \n"
                    + "\n"
                    + ":clock: Mulai pada  : %s \n"
                    + ":clock: Selesai pada: %s \n"
                    + "\n"
                    + "Deskripsi Event anda adalah %s", link, id, tglAwal, tglSelesai, deskripsi);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            String link = newEvent.getHtmlLink();
            String id = newEvent.getId();
            String tglAwal = newEvent.getStart().getDateTime().toString();
            String tglSelesai = newEvent.getEnd().getDateTime().toString();
            String deskripsi = newEvent.getDescription();
            String ret = String.format("Event Berhasil di-update \n"
                    + "Berikut link event baru anda: [LINK](%s) \n"
                    + ":id: %s \n"
                    + "\n"
                    + ":clock: Mulai pada  : %s \n"
                    + ":clock: Selesai pada: %s \n"
                    + "\n"
                    + "Deskripsi Event anda adalah %s", link, id, tglAwal, tglSelesai, deskripsi);
            return ret;

        } catch (Exception e) {
            e.printStackTrace();
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public com.bot.insched.model.Event save(com.bot.insched.model.Event event) {
        return eventRepository.save(event);
    }

    public com.bot.insched.model.Event findById(String id) {
        return eventRepository.findByIdEvent(UUID.fromString(id));
    }
}
