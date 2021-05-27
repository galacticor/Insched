package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.ShowCalendarService;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
class ShowCalendarCommandTest {
    @Mock
    ShowCalendarService showCalendarService;

    @InjectMocks
    ShowCalendarCommand showCalendarCommand;

    @Mock
    PrivateMessageReceivedEvent privateMessageevent;

    @Mock
    MessageSender sender;

    @Mock
    Event event;

    @Mock
    Events events;


    private String start_date = "2000-04-22T15:30:00.000-07:00";
    private String end_date = "2000-04-23T15:30:00.000-07:00";

    @Mock
    private EventDateTime eventDateTimeMulai;

    @Mock
    private EventDateTime eventDateTimeSelesai;

    @Mock
    private DateTime dateTimeMulai;

    @Mock
    private DateTime dateTimeSelesai;

    @Mock
    private EmbedBuilder embedBuilder;

    private  List<Event> listEvent;


    @BeforeEach
    public void setUp() {
        User user = mock(User.class);
        ReflectionTestUtils.setField(showCalendarCommand, "sender", sender);
        lenient().when(privateMessageevent.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");

        listEvent = new ArrayList<>();
        event = new Event();
        event.setSummary("Tes 1").setDescription("description");
        dateTimeMulai = new DateTime(start_date);
        eventDateTimeMulai = new EventDateTime().setDateTime(dateTimeMulai);
        dateTimeSelesai = new DateTime(end_date);
        eventDateTimeSelesai = new EventDateTime().setDateTime(dateTimeSelesai);
        event.setStart(eventDateTimeMulai).setEnd(eventDateTimeSelesai);
        listEvent.add(event);

        events = new Events();
        events.setNextPageToken("1234").setItems(listEvent);

    }


    @Test
    public void testExecute(){
        String[] args = {};
        lenient().doNothing().when(sender).sendPrivateMessage("Selamat datang di fitur ShowCalendar! "
                + "Di bawah ini adalah kalender kamu", privateMessageevent);
        lenient().when(privateMessageevent.getMessage()).thenReturn(mock(Message.class));
        lenient().when(privateMessageevent.getMessage().getAuthor()).thenReturn(mock(User.class));
        showCalendarCommand.execute(args, privateMessageevent);
    }

    @Test
    void testCreateEmbed(){
        String [] args = {};;
        InschedEmbed embed = new InschedEmbed();
        try {
            lenient().when(showCalendarService.getListEvents("userId")).thenReturn(listEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lenient().when(showCalendarService.getCalSummary(listEvent.get(0))).thenReturn("Tes 1");
        lenient().when(showCalendarService.getCalDescription(listEvent.get(0))).thenReturn("description");
//        lenient().when(embed.addField("lala", "lili", false)).thenReturn(embedBuilder);
        assertNotNull(showCalendarCommand.createEmbed("userId",privateMessageevent));

    }

    @Test
    public void getCalSummarySuccess(){
        lenient().when(showCalendarService.getCalSummary(event)).thenReturn("123");
        String res = showCalendarService.getCalSummary(event);
        assertNotNull(res);
    }


    @Test
    void testGetHelp() {
        assertNull(showCalendarCommand.getHelp());
    }

    @Test
    void testGetCommand() {
        assertEquals("showCalendar", showCalendarCommand.getCommand());
    }

}
