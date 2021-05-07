
package com.bot.insched.service;

import com.bot.insched.google.GoogleAPIManager;
import com.bot.insched.repository.EventRepository;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{
    @Autowired
    private GoogleAPIManager manager;

    @Autowired
    EventRepository eventRepository;

    @Override
    public String getEventService(String discordId, String eventId){
        Calendar calendar = manager.getCalendarService(discordId);
        if(calendar == null){
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            Event event = calendar.events().get("primary",eventId).execute();
            return "Event link anda adalah " + event.getHtmlLink();
        }catch (Exception e){
            e.printStackTrace();
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public Calendar getCalendarbyId(String discordId) {
        return manager.getCalendarService(discordId);
    }

    @Override
    public String createEventService(String discordId,Event event) {
        Calendar calendar = getCalendarbyId(discordId);
        if(calendar == null){
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            Event newEvent = calendar.events().insert("primary",event).execute();
            String link = newEvent.getHtmlLink();
            String id = newEvent.getId();
            return "Event Berhasil dibuat \n" + link +"\n"+
                    "Event id anda adalah " + id;
        }catch (Exception e){
            e.printStackTrace();
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public String deleteEventService(String discordId,String eventId) {
        Calendar calendar = getCalendarbyId(discordId);
        if(calendar == null){
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            calendar.events().delete("primary",eventId).execute();
            return "Event berhasil terhapus!";
        }catch (Exception e){
            e.printStackTrace();
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public String updateEventService(String discordId,String eventId,Event event) {
        Calendar calendar = getCalendarbyId(discordId);
        if(calendar == null){
            return "Silahkan login terlebih dahulu menggunakan !login";
        }
        try {
            Event newEvent = calendar.events().update("primary",eventId,event).execute();
            return "Event berhasil di-Update \n"+
                    "link event anda: \n"+ newEvent.getHtmlLink();
        }catch (Exception e){
            e.printStackTrace();
            return "Terjadi kesalahan pastikan anda memasukkan input dengan benar";
        }
    }

    @Override
    public com.bot.insched.model.Event save(com.bot.insched.model.Event event) {
        return eventRepository.save(event);
    }
}
