package com.bot.insched.discord.invoker;

import com.bot.insched.discord.command.Command;
import com.bot.insched.discord.command.CreateAppointmentSlot;
import com.bot.insched.discord.command.ShowMyAppointment;
import com.bot.insched.discord.receiver.Receiver;
import com.bot.insched.model.Appointment;
import com.bot.insched.service.AppointmentService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InvokerTest {
    @InjectMocks
    Invoker invoker;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    AppointmentService appointmentService;

    @Mock
    Receiver receiver;

    @InjectMocks
    ShowMyAppointment comm;



    // Basic setup
    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;
    private static User jdaUser;

    private LocalDate start;
    private LocalDate end;
    private Appointment app;



    @BeforeAll
    public static void init() throws Exception{
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            jdaUser = user;
        });
    }

    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(500);
        init();
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
        start = LocalDate.parse("2024-05-03");
        end = LocalDate.parse("2024-05-10");
        app = new Appointment("testing", start, end);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(3000);
    }

    @Test
    public void testExecute() throws Exception{
        message = new MessageBuilder().append("!myAppointment").build();
        List<Appointment> listApp = new ArrayList<>();
        listApp.add(app);

        lenient().when(event.getMessage()).thenReturn(message);
        lenient().when(appointmentService.getAllUserAppointment(anyString()))
                .thenReturn(listApp);

        invoker.onPrivateMessageReceived(event);

    }

}
