package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.BookingAppointmentService;
<<<<<<< HEAD
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
=======
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BookAppointmentCommandTest {

    @InjectMocks
    BookAppointmentCommand command;

    @Mock
    BookingAppointmentService service;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    private MessageSender sender;

<<<<<<< HEAD
    private String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
    private String dummyId = "0";
=======
    private final String dummyId = "0";
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(command, "sender", sender);
        User user = mock(User.class);

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn(dummyId);
    }

    @Test
<<<<<<< HEAD
    public void testExecuteSuccess() throws Exception {;
        String args[] = {dummyToken};
=======
    public void testExecuteSuccess() throws Exception {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String[] args = {dummyToken};
>>>>>>> 732c0d5bd97def3df6fc0fded00a4f6bca3f597f
        String res = "Booking slot event telah dibuat!";
        lenient().when(service.createBooking(dummyId, dummyToken)).thenReturn(res);
        command.execute(args, event);
    }


    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        assertEquals(res, "Digunakan untuk membuat booking pada slot event dalam sebuah appointment.\n" +
                "Penggunaan: !bookAppointment <token_event>\n" +
                "Contoh: !bookAppointment e79e7cf1-0b8c-48db-a05b-baafcb5953d2");
    }

    @Test
    public void testGetCommand() {
        assertEquals(command.getCommand(), "bookAppointment");
    }
}
