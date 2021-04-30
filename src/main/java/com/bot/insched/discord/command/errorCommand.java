package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class errorCommand implements Command{
    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Command yang anda masukkan Salah \n" +
                    "!help untuk mengetahui command setiap fitur yang tersedia").queue();
        });
    }

    @Override
    public String getCommand() {
        return "error";
    }
}
