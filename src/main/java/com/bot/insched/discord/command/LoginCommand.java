package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class LoginCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();
    private GoogleService service;

    public LoginCommand(GoogleService service) {
        this.service = service;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        String url = service.getAuthorizationUrl(userId);

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Login");
        embed.setDescription(String.format("Silakan login melalui link berikut [LINK](%s)", url));

        sender.sendPrivateMessage(embed.build(), event);
    }

    @Override
    public String getCommand() {
        return "login";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
