package com.bot.insched;

import com.bot.insched.discord.invoker.Invoker;
import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InschedApplication {
    public static JDA jda;
    private static String TOKEN;

    @Autowired
    private Invoker botListener;
    @Autowired
    private SchedulerConfig scheduler;

    @Autowired
    public InschedApplication(@Value("${discord_token}") String token) {
        TOKEN = token;
    }

    public static void main(String[] args) throws LoginException {
        SpringApplication.run(InschedApplication.class, args);
    }

    @PostConstruct
    public void run() throws LoginException {
        jda = JDABuilder.createDefault(TOKEN).build();
        jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.addEventListener(botListener);

        scheduler.doScheduler();
    }

    public static JDA getJda() {
        return jda;
    }

}
