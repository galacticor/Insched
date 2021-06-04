package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.Event;
import com.bot.insched.service.BookingAppointmentService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewSlotCommandTest {

    @InjectMocks
    ViewSlotCommand command;

    @Mock
    BookingAppointmentService service;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    private MessageSender sender;

    private final String dummyId = "0";

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(command, "sender", sender);
        User user = mock(User.class);

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn(dummyId);
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String[] args = {dummyToken};
        List<Event> eventList = new ArrayList<>();
        lenient().when(service.viewHostBookingSlots(dummyId, dummyToken)).thenReturn(eventList);
        command.execute(args, event);
    }

    @Test
    public void testInsufficientArgumentExecution() {
        String emptyString = "";
        String[] args = {emptyString};
        String res =
                "Masukkan argumen yang sesuai!\n"
                        + "Penggunaan: !bookAppointment <token_event>\n"
                        + "Help: !bookAppointment help";
        lenient().doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testGeneralException() throws Exception {
        String dummyToken = "e79e7cf1-0b8c-48db-a05b-baafcb5953d2";
        String[] args = {dummyToken};

        when(service.viewHostBookingSlots(dummyId, dummyToken))
                .thenThrow(new Exception("dummy exception"));
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        assertEquals(res, "Menampilkan semua slot milik Host\n" +
                "Penggunaan: !viewSlot <host-token>\n" +
                "Contoh: !viewSlot f2da393a-7ef2-4fe9-979e-ea3d76adc7ea");
    }



    @Test
    public void testGetCommand() {
        assertEquals(command.getCommand(), "viewSlot");
    }
}
