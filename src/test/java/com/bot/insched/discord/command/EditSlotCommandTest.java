package com.bot.insched.discord.command;

import com.bot.insched.discord.exception.NotLoggedInException;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
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
public class EditSlotCommandTest {

    @InjectMocks
    EditSlotCommand command;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    AppointmentService appointmentService;

    @Mock
    private MessageSender sender;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(command, "sender", sender);
    }


    @Test
    public void testExecuteSuccess() throws Exception {
        String[] args = {"dummy_token", "15:30", "30", "dummy_judul", "123"};
        User user = mock(User.class);
        String res = "Slot berhasil di-update";

        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
        lenient().doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testExecuteGetException() throws Exception{
        String[] args = {"dummy_token", "15:30", "30", "dummy_judul", "123"};
        lenient().when(appointmentService.editSlot(anyString(), anyString(), anyInt(), anyString(), anyString()))
            .thenThrow(new NotLoggedInException());
        command.execute(args, event);
    }


    @Test
    public void testExecuteInsufficientArgument() {
        String[] args = {"dummy"};
        command.execute(args, event);
    }

    @Test
    public void testExecuteHelpCommand() {
        String[] args = {"help"};
        User user = mock(User.class);
        String res = "Digunakan untuk update judul dan jam appointment.\n" +
            "Hanya dapat mengupdate appointment yang belum dibooking.\n\n" +
            "Penggunaan: !editSlot token_slot jam_baru durasi_baru judul_baru\n\n" +
            "Contoh: !editSlot 94a56007-cbe4-47a0-aa54-2c0689c7e19c 17:00 30 Sprint_report\n\n";


        lenient().when(event.getAuthor()).thenReturn(user);
        lenient().when(user.getId()).thenReturn("123");
        lenient().doNothing().when(sender).sendPrivateMessage(res, event);
        command.execute(args, event);
    }

    @Test
    public void testGetHelp() {
        String expected = "Digunakan untuk update judul dan jam appointment.\n" +
            "Hanya dapat mengupdate appointment yang belum dibooking.\n\n" +
            "Penggunaan: !editSlot token_slot jam_baru durasi_baru judul_baru\n\n" +
            "Contoh: !editSlot 94a56007-cbe4-47a0-aa54-2c0689c7e19c 17:00 30 Sprint_report\n\n";

        assertEquals(expected, command.getHelp());
    }

}
