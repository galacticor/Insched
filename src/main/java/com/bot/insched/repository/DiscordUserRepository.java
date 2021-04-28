package com.bot.insched.repository;

import com.bot.insched.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordUserRepository extends JpaRepository<DiscordUser, String> {
}
