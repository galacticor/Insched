package com.bot.insched.discord.command;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;


public class ErrorCommand implements Command {
    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        sendPrivateMessage(getHelp(), event);
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

    private void sendPrivateMessage(String response, PrivateMessageReceivedEvent event) {
        event.getAuthor().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(response).queue();
        });
    }
}
