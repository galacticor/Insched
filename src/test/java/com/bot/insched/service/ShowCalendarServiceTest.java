package com.bot.insched.service;
import com.bot.insched.discord.command.ShowCalendarCommand;
import com.bot.insched.google.GoogleAPIManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;


//@ExtendWith(MockitoExtension.class)
//public class ShowCalendarServiceTest {
//    ShowCalendarService showCalendarService;
//
//    @Test
//    public void testGetCalendarServiceNull(){
//        String userId = null;
//        assertNull(showCalendarService.getCalService(userId));
//    }

//    @Test
//    public void testGetSummary(){
//        assertNull(showCalendarService.getCalSummary(event));
//    }
//
//    @Test
//    public void testGetDescription(){
//        assertNull(showCalendarService.getCalDescription(event));
//    }
}
