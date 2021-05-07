package com.bot.insched.service;
import com.bot.insched.discord.command.ShowCalendarCommand;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.ShowCalendarService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
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
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ShowCalendarServiceTest {
    @Mock
    ShowCalendarService showCalendarService;

    @InjectMocks
    ShowCalendarCommand showCalendarCommand;

    @Mock
    PrivateMessageReceivedEvent privateMessageReceivedEvent;

    private static JDA jda;
    private static String messageId = "838918902233956383";
    private static String userId = "461191404341821455";
    private static long responseNumber = 8;
    private static String channelId = "836884748667846718";
    private static Message message;
    private static User jdaUser;


    @Mock
    Event event;

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

    // Thread.sleep to delay execution and prevent error
    @AfterAll
    public static void teardown() throws Exception {
        jda.shutdownNow();
        Thread.sleep(2000);
    }


    @BeforeEach
    public void setUp() throws Exception{
        Thread.sleep(1000);
//        init();
        lenient().when(privateMessageReceivedEvent.getAuthor()).thenReturn(jdaUser);
        lenient().when(privateMessageReceivedEvent.getMessage()).thenReturn(message);
    }

//    @Test
//    public void testGetCalService() {
//        String userId = privateMessageReceivedEvent.getMessage().getAuthor().getId();
//        assertNull(showCalendarService.getCalService(userId));
//    }
}
