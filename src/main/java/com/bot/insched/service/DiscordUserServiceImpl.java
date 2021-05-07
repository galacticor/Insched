package com.bot.insched.service;

import com.bot.insched.model.DiscordUser;
import com.bot.insched.repository.DiscordUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordUserServiceImpl implements DiscordUserService{

    @Autowired
    DiscordUserRepository discordUserRepository;

    public DiscordUser findByUserId(String idDiscord) {
        return discordUserRepository.findByIdDiscord(idDiscord);
    }

    @Override
    public DiscordUser save(DiscordUser user) {
        return discordUserRepository.save(user);
    }


}
