package com.bot.insched.discord.command;

import com.bot.insched.discord.exception.SlotUnavailableException;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.AppointmentService;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteSlotCommandTest {

    @InjectMocks
    DeleteSlotCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    AppointmentService appointmentService;


    @Mock
    MessageSender sender;


    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(command, "sender", sender);
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String[] args = {"12f8ca5e-8888-45b8-881b-a58d349e5269"};
        User user = mock(User.class);
        String res = "Slot berhasil dihapus!";

        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("123");
        when(appointmentService.deleteSlot(args[0],"123")).thenReturn(res);
        doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testHelpCommand() throws Exception {
        String[] args = {"help"};
        User user = mock(User.class);
        String res = "Digunakan untuk menghapus appointment yang belum dibooking.\n" +
            "Penggunaan: !deleteSlot slot_token\n" +
            "Contoh: !deleteSlot 12f8ca5e-8888-45b8-881b-a58d349e5269";

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
        doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }


    @Test
    public void testCatchException() throws Exception {
        String[] args = {"dummy_token"};
        User user = mock(User.class);
        String res = "Tidak ada slot pada keterangan waktu seperti itu!";

        when(event.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn("123");
        when(appointmentService.deleteSlot(args[0],"123"))
            .thenThrow(new SlotUnavailableException("Tidak ada slot pada keterangan waktu seperti itu!"));
        doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "Digunakan untuk menghapus appointment yang belum dibooking.\n" +
        "Penggunaan: !deleteSlot slot_token\n" +
            "Contoh: !deleteSlot 12f8ca5e-8888-45b8-881b-a58d349e5269";
        assertEquals(expected, command.getHelp());
    }

    @Test
    public void testGetCommand() {
        String expected = "deleteSlot";
        assertEquals(expected, command.getCommand());
    }

}
