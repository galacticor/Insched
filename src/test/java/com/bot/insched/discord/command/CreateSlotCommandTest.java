package com.bot.insched.discord.command;

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
import org.springframework.core.annotation.Order;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Order
public class CreateSlotCommandTest {

    @InjectMocks
    CreateSlotCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    MessageSender sender;

    @Mock
    AppointmentService appointmentService;



    @BeforeEach
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(command, "sender", sender);
        User user = mock(User.class);

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String[] args = { "2021-05-08", "15:30", "30", "2", "DEMO_TP" };
        String tanggal = args[0];
        String jamMulai = args[1];
        int durasi = Integer.parseInt(args[2]);
        int kapasitas = Integer.parseInt(args[3]);
        String deskripsi = args[4];
        String res = "Slot berhasil dibuat!";

        lenient().when(appointmentService.createSlot(tanggal, jamMulai, durasi, kapasitas, deskripsi))
            .thenReturn(res);

        command.execute(args, event);
    }

    @Test
    public void testInsufficientArgumentExecution() throws Exception {
        String[] args = { "2021-05-08", "15:30", "30", "2"};
        String res = "Masukkan argumen yang sesuai!";
        lenient().doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testGeneralException() throws Exception {
        String[] args = { "2021-05-08", "15:30", "30", "2", "DEMO_TP" };
        String tanggal = args[0];
        String jamMulai = args[1];
        int durasi = Integer.parseInt(args[2]);
        int kapasitas = Integer.parseInt(args[3]);
        String deskripsi = args[4];
        String res = "dummy exception";

        when(appointmentService.createSlot(tanggal, jamMulai, durasi, kapasitas, deskripsi))
            .thenThrow(new Exception("dummy exception"));
        command.execute(args, event);
    }

    @Test
    public void testGetCommand() {
        String res = command.getCommand();
        assertEquals(res, "createSlot");
    }

    @Test
    public void testGetHelp() {
        String res = command.getHelp();
        String expected = "!createSlot tanggal jam_mulai durasi(menit) kapasitas deskripsi\n"
                + "Contoh: !createSlot 2022-02-03 15:30 30 2 meeting_startup";

        assertEquals(res, expected);
    }
}
