package com.bot.insched.discord.receiver;

import com.bot.insched.discord.command.Command;
import com.bot.insched.model.Appointment;
import com.bot.insched.service.AppointmentService;
import jdk.vm.ci.meta.Local;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ReceiverTest {

    @InjectMocks
    Receiver receiver;

    @Mock
    AppointmentService appointmentService;

    @Mock
    PrivateMessageReceivedEvent event;

    //Basic test setup
    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;
    private static User jdaUser;


    @BeforeAll
    public static void init() throws Exception {
        // Thread sleep for delaying test execution, prevent errors
//        Thread.sleep(2000);
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            user.openPrivateChannel().queue(privateChannel -> {
                jdaUser = privateChannel.getUser();
                privateChannel.retrieveMessageById(messageId).queue(message1 -> {
                    message = message1;
                });
            });
        });

    }

    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(500);
        init();
        lenient().when(event.getMessage()).thenReturn(message);
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

    @Test
    public void testGetCommand() {
        Command comm = receiver.getCommand("!auth");
        assertEquals(receiver.getCommand("!auth"), comm);
    }

    @Test
    public void testExecuteSuccess() {
        message = new MessageBuilder().append("!myAppointment").build();
        LocalDate start = LocalDate.parse("2024-05-03");
        LocalDate end = LocalDate.parse("2024-10-03");
        Appointment app = new Appointment("ngetest", start, end);
        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(app);

        lenient().when(appointmentService.getAllUserAppointment(anyString())).thenReturn(appointmentList);

        when(event.getMessage()).thenReturn(message);
        receiver.execute(event);
    }

    @Test
    public void testExecuteFalsePrefix() {
        message = new MessageBuilder().append(">myAppointment").build();
        when(event.getMessage()).thenReturn(message);
        receiver.execute(event);
    }

    @Test
    public void testCommandNotExists() {
        message = new MessageBuilder().append("!dummy").build();
        when(event.getMessage()).thenReturn(message);
        receiver.execute(event);
    }



}
