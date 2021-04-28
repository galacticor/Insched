package com.bot.insched.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public interface Command {

    void execute(String[] args, PrivateMessageReceivedEvent event);

    String getCommand();

}
