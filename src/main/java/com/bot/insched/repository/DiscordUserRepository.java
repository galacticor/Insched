package com.bot.insched.repository;

import com.bot.insched.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, String> {
    DiscordUser findByIdDiscord(String id);
    DiscordUser findByGoogleToken(String token);
}
