package com.bot.insched.discord.command;

import com.bot.insched.model.Appointment;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import com.google.api.client.auth.oauth2.StoredCredential;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase
public class CreateAppointmentCommandTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private DiscordUserService discordUserService;

    @Mock
    private PrivateMessageReceivedEvent event;

    @InjectMocks
    private CreateAppointmentCommand command;


    // Basic test setup
    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;

    private DiscordUser user;
    private StoredCredential storedCredential;
    private static User jdaUser;


    @BeforeAll
    public static void init() throws Exception {
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
    public void setUp() throws Exception {
        Thread.sleep(500);
        init();
        storedCredential = new StoredCredential();
        storedCredential.setAccessToken("dummy");
        storedCredential.setRefreshToken("dummy");
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
        lenient().when(event.getMessage()).thenReturn(message);
    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

    @Test
    public void testCorrectArgument() throws Exception {
        String[] args = {"testing_new", "2024-05-03", "2024-05-03"};
        LocalDate start = LocalDate.parse(args[1]);
        LocalDate end = LocalDate.parse(args[2]);
        Appointment appointment = new Appointment(args[0], start, end);
        appointment.setOwner(user);
        appointmentService.save(appointment);

        lenient().when(appointmentService.createAppointment(any(), any(), any(), any()))
                .thenReturn("Appointment berhasil dibuat");
        lenient().when(appointmentService.findUserById(anyString())).thenReturn(user);
        command.execute(args, event);
    }


    @Test
    public void testFalseArgument() {
        String[] args = {"dummy", "dummy", "dummy"};
        command.execute(args, event);
    }

    @Test
    public void testHelpArgument() {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "!createAppointment <deskripsi_appointment> <tanggal_mulai> <tanggal_selesai>\n" +
                "Contoh: !createAppointment DEMO_TP3 2021-05-03 2021-05-15";
        assertEquals(command.getHelp(), expected);
    }

    @Test
    public void testGetCommand() {
        assertEquals(command.getCommand(), "createAppointment");
    }

}
