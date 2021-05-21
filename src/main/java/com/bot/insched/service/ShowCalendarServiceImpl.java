package com.bot.insched.service;

import com.bot.insched.google.GoogleApiManager;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.Calendar.Events.List;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowCalendarServiceImpl implements ShowCalendarService {

    @Autowired
    GoogleApiManager manager;

    public List<Event> getListEvents(String userId) {
        Calendar service = manager.getCalendarService(userId);
        List<Event> listEventAll = new ArrayList<>();

        String pageToken = null;
        do {
            Events events = null;
            try {
                events = service.events().list("primary").setPageToken(pageToken).execute();
                listEventAll = events.getItems();
                pageToken = events.getNextPageToken();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (pageToken != null);

        List<Event> listEvent = get10LatestEvent(listEventAll);
        return listEvent;
    }

    public String getCalSummary(Event event) {
        return event.getSummary();
    }

    public String getCalDescription(Event event) {
        String description = event.getDescription();
        if(description == null){
            description = "no description";
        }else if(description.length() >= 1000){
            description = description.substring(0,90) + "...";
        }
        return description;
    }

    public String getCalStart(Event event){
        return event.getStart().getDateTime().toString();
    }

    public String getCalEnd(Event event){
        return event.getEnd().getDateTime().toString();
    }

    public List<Event> get10LatestEvent(List<Event> listEvent) throws IndexOutOfBoundsException{
        List<Event> list10Event = new ArrayList<>();
        if(listEvent.size()<10){
//            System.out.println("masuk get10LatestEvent dengan length kurang dari 10");
            for (int i = listEvent.size()-1 ; i >= 0 ; i--) {
                list10Event.add(listEvent.get(i));
            }
        }else{
//            System.out.println("masuk get10LatestEvent dengan length lebih dari 10");
            for (int i = listEvent.size()-1 ; i >= listEvent.size()-9 ; i--) {
                list10Event.add(listEvent.get(i));
            }
        }
//        System.out.println("Array setelah dipilih 10 = " + Arrays.asList(list10Event).toString());
        return list10Event;
    }







}




