package com.bot.insched.discord.command;

import com.bot.insched.model.Appointment;
import com.bot.insched.service.AppointmentService;
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
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
@Order
public class ShowMyAppointmentCommandTest {

    @Mock
    AppointmentService appointmentService;

    @InjectMocks
    ShowMyAppointment showMyAppointment;

    @Mock
    PrivateMessageReceivedEvent privateMessageReceivedEvent;

    // Basic test setup
    private static JDA jda;
    private static String userId = "461191404341821455";
    private static Message message;
    private static User jdaUser;

    LocalDate start;
    LocalDate end;

    @BeforeAll
    public static void init() throws Exception {
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            jdaUser = user;
        });
    }

    // Thread.sleep to delay execution and prevent error
    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }


    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);

        start = LocalDate.parse("2021-10-08");
        end = LocalDate.parse("2021-12-10");
        lenient().when(privateMessageReceivedEvent.getAuthor()).thenReturn(jdaUser);
        lenient().when(privateMessageReceivedEvent.getMessage()).thenReturn(message);
    }

    @Test
    public void testExecute() {
        String[] args = {};
        Appointment app = new Appointment("ngetest", start, end);
        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(app);

        when(appointmentService.getAllUserAppointment(anyString())).thenReturn(appointmentList);
        showMyAppointment.execute(args, privateMessageReceivedEvent);
    }

    @Test
    public void testHelp() {
        String[] args = {"help"};
        showMyAppointment.execute(args, privateMessageReceivedEvent);
    }

    @Test
    public void testGetHelp() {
        assertEquals(showMyAppointment.getHelp(), null);
    }

    @Test
    public void testGetCommand() {
        assertEquals(showMyAppointment.getCommand(), "myAppointment");
    }
}
