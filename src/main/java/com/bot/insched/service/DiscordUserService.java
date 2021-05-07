package com.bot.insched.service;

import com.bot.insched.model.DiscordUser;

public interface DiscordUserService {
    DiscordUser findByUserId(String idDiscord);
    DiscordUser save(DiscordUser user);
}
