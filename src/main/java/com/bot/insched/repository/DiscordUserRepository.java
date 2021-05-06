package com.bot.insched.repository;

import com.bot.insched.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, String> {
    DiscordUser findByIdDiscord(String id);
    DiscordUser findByAccessToken(String token);

    @Query(value = "select key from discord_user", nativeQuery = true)
    Set<String> findAllKeys();
}
