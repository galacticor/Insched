package com.bot.insched.repository;

import com.bot.insched.model.DiscordUser;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscordUserRepository extends JpaRepository<DiscordUser, String> {
    DiscordUser findByIdDiscord(String id);

    DiscordUser findByAccessToken(String token);

    @Query(value = "select key from discord_user", nativeQuery = true)
    Set<String> findAllKeys();
}
