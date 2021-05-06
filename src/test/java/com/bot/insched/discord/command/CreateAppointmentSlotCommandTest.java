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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CreateAppointmentSlotCommandTest {

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    AppointmentService appointmentService;

    @InjectMocks
    CreateAppointmentSlot command;

    // Basic setup
    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;
    private static User jdaUser;

    private String[] args;
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

    // Thread.sleep to delay execution and prevent error
    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
        start = LocalDate.parse("2024-05-03");
        end = LocalDate.parse("2024-05-10");
        app = new Appointment("testing", start, end);
        args = new String[]{"dummy_token", "2024-05-03", "15:30", "30", "2"};
    }

    @AfterAll
    public static void tearDown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        when(appointmentService.findAppointmentById(any())).thenReturn(app);
        when(command.handleCreation(args))
                .thenReturn("Slot berhasil dibuat!");

        command.execute(args, event);
    }

    @Test
    public void testAppointmentNotExists() throws Exception {
        when(appointmentService.findAppointmentById(any())).thenReturn(null);
        command.execute(args, event);
    }

    @Test
    public void testInvalidDate() throws Exception {
        LocalDate start = LocalDate.parse("2024-04-03");
        LocalDate end = LocalDate.parse("2024-04-10");
        Appointment app = new Appointment("testing", start, end);
        when(appointmentService.findAppointmentById(any())).thenReturn(app);
        command.execute(args, event);
    }

    @Test
    public void testHelpCommand() throws Exception {
        String[] args = {"help"};
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "!createSlot <token_appointment> <tanggal> <jam_mulai> <durasi (menit)> <kapasitas_attendee>\n" +
                "Contoh: !createSlot 1234-rsrf0-ir9-3 2021-05-20 15:30 30 2";

        assertEquals(expected, command.getHelp());
    }

    @Test
    public void testGetCommand() {
        String expected = "createSlot";
        assertEquals(command.getCommand(), expected);
    }





}
