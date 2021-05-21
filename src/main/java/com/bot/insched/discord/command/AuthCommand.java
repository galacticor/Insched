package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.GoogleService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class AuthCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();
    private GoogleService service;

    public AuthCommand(GoogleService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        String reply = service.authToken(userId, args[0]);

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Authentication Status");
        embed.setDescription(reply);

        sender.sendPrivateMessage(embed.build(), event);
    }

    @Override
    public String getCommand() {
        return "auth";
    }

    @Override
    public String getHelp() {
        return "Masukkan dengan format seperti berikut \n"
            + "!auth <token>";
    }
}
