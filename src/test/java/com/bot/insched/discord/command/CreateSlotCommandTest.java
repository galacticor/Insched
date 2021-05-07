package com.bot.insched.discord.command;

import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
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
import org.springframework.core.annotation.Order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Order
public class CreateSlotCommandTest {

    @InjectMocks
    CreateSlotCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    DiscordUserService discordUserService;

    @Mock
    AppointmentService appointmentService;

    // Basic test setup
    private static JDA jda;
    private static String userId = "461191404341821455";
    private static Message message;
    private static User jdaUser;

    @BeforeAll
    public static void init() throws Exception {
        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
        jda.retrieveUserById(userId).queue(user -> {
            jdaUser = user;
        });
        message = new MessageBuilder().append("dummy").build();
        Thread.sleep(2000);

    }

    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }


    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);
        lenient().when(event.getAuthor()).thenReturn(jdaUser);
    }

    @Test
    @Order(1)
    public void testExecuteSuccess() throws Exception{
        String[] args = {"2021-05-08", "15:30", "30", "2", "DEMO_TP"};
        command.execute(args, event);
    }

    @Test
    @Order(2)
    public void testInsufficientArgumentExecution() throws Exception {
        String[] args = {"2021-05-08", "15:30", "30", "2"};
        command.execute(args, event);
    }

    @Test
    @Order(3)
    public void testNotLoggedIn() throws Exception {
        String[] args = {"2021-05-08", "15:30", "30", "2", "DEMO_TP"};
        command.execute(args, event);
    }

    @Test
    @Order(4)
    public void testGetCommand() {
        String res = command.getCommand();
        assertEquals(res, "createSlot");
    }

    @Test
    @Order(5)
    public void testGetHelp() {
        String res = command.getHelp();
        String expected = "!createSlot tanggal jam_mulai durasi(menit) kapasitas deskripsi" +
                "Contoh: !createSlot 2022-02-03 15:30 30 2 meeting_startup";

        assertEquals(res, expected);
    }
}