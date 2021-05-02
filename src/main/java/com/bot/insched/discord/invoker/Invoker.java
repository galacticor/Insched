package com.bot.insched.discord.invoker;

import com.bot.insched.discord.receiver.Receiver;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Invoker extends ListenerAdapter {
	@Autowired
    private Receiver receiver;

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        receiver.execute(event);
    }
}
