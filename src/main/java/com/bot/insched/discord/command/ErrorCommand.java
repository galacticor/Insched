package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class ErrorCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sender.sendPrivateMessage(getHelp(), event);
    }

    @Override
    public String getCommand() {
        return "error";
    }

    @Override
    public String getHelp() {
        return "Command yang anda masukkan Salah \n"
            + "!help untuk mengetahui command setiap fitur yang tersedia";
    }
}
