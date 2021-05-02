package com.bot.insched;

import com.bot.insched.discord.invoker.Invoker;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@SpringBootApplication
public class InschedApplication {
	public static JDA jda;

	@Autowired
	private Invoker botListener;

	public static void main(String[] args) throws LoginException {
		SpringApplication.run(InschedApplication.class, args);
	}

	@PostConstruct
	public void run() throws LoginException {
		jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
		jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
		jda.addEventListener(botListener);
	}

}
