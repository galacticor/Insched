package com.bot.insched.invoker;

import com.bot.insched.receiver.Receiver;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Invoker extends ListenerAdapter {

    public final Receiver receiver = new Receiver();

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        receiver.execute(event);
    }
}
