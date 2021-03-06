package com.bot.insched.discord.command;

import com.bot.insched.discord.util.MessageSender;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.service.AppointmentService;
import com.bot.insched.service.DiscordUserService;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class MyTokenCommand implements Command {
    private MessageSender sender = MessageSender.getInstance();
    private AppointmentService appointmentService;
    private DiscordUserService discordUserService;

    public MyTokenCommand(AppointmentService appointmentService,
                          DiscordUserService discordUserService) {
        this.appointmentService = appointmentService;
        this.discordUserService = discordUserService;
    }

    @Override
    public void execute(String[] args, PrivateMessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        DiscordUser user = discordUserService.findByUserId(userId);
        String res;
        if (user == null) {
            res = "Kamu belum login. Silahkan login menggunakan !login";
        } else {
            res = "Token kamu adalah: " + appointmentService.getUserToken(user);
        }
        sender.sendPrivateMessage(res, event);
    }

    @Override
    public String getCommand() {
        return "myToken";
    }

    @Override
    public String getHelp() {
        return "!myToken";
    }
}
