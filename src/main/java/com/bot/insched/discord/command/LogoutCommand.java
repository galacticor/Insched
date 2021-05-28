package com.bot.insched.discord.command;

import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.service.GoogleService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class LogoutCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();
    private GoogleService service;
    private DiscordUserService discordService;

    public LogoutCommand(GoogleService service,
                        DiscordUserService discordService) {
        this.service = service;
        this.discordService = discordService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        String userInfo = service.getUserInfo(userId);

        InschedEmbed embed = new InschedEmbed();
        embed.setTitle("Good Bye");
        embed.setDescription(String.format("Terimakasih sudah menggunakan bot Insched, selamat tinggal %s", userInfo));

        discordService.logout(userId);
        sender.sendPrivateMessage(embed.build(), event);
    }

    @Override
    public String getCommand() {
        return "logout";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
