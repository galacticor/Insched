package com.bot.insched.discord.invoker;

import com.bot.insched.discord.receiver.Receiver;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvokerTest {
    @InjectMocks
    Invoker invoker;

    @Mock
    PrivateMessageReceivedEvent event;

    @Mock
    Receiver receiver;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testExecute() {
        ReflectionTestUtils.setField(invoker, "receiver", receiver);
        doNothing().when(receiver).execute(any(PrivateMessageReceivedEvent.class));

        invoker.onPrivateMessageReceived(event);
    }

}
