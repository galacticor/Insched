package com.bot.insched;

import com.bot.insched.discord.task.TaskFactory;
import com.bot.insched.discord.util.InschedEmbed;
import com.bot.insched.model.DiscordUser;
import com.bot.insched.model.Event;
import com.bot.insched.repository.EventRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class SchedulerConfig {
    private EventRepository eventRepository;
    private TaskFactory taskFactory;
    private TaskScheduler scheduler;

    public SchedulerConfig(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.taskFactory = new TaskFactory();
        this.scheduler = new ConcurrentTaskScheduler(
            Executors.newScheduledThreadPool(2));
    }

    public void doScheduler() {
        doScheduleOneMinute();
        doScheduleTenMinute();
    }

    @Async
    @Scheduled(fixedRate = 60000) // every 1 minutes
    public void doScheduleOneMinute() {
        doCheckEventNotif(1, 1);
        doCheckEventNotif(10, 1);
        doCheckEventNotif(30, 1);
        doCheckEventNotif(60, 1);
    }

    @Async
    @Scheduled(fixedRate = 600000) // every 10 minutes
    public void doScheduleTenMinute() {

    }

    /*
    Fungsi ini untuk menjalankan pengiriman notif event setiap @every minutes
    dan notifnya dikirim @minutes menit sebelum event dimulai
    */
    @Async
    public void doCheckEventNotif(int minutes, int every) {
        log.info("running check event notif for {} minutes", minutes);
        List<Event> events = eventRepository.findAllByStartTimeBetween(
            LocalDateTime.now().plusMinutes(minutes),
            LocalDateTime.now().plusMinutes(minutes + every));

        log.info("{} event found", events.size());
        for (Event event : events) {
            log.info("checking event [{}]", event.getIdEvent());
            
            List<DiscordUser> listAttendee = event.getListAttendee();
            if (listAttendee.size() == 0) {
                continue;
            }

            String message = "Kamu memiliki appointment pada "
                + event.getWaktu() + " , jangan lupa untuk hadir !!";
            InschedEmbed embed = new InschedEmbed();
            embed.setTitle("Notification");
            embed.setDescription(message);

            Date date = Date.from(event.getStartTime()
                            .minusMinutes(minutes)
                            .atZone(ZoneId.of("Asia/Jakarta"))
                            .toInstant());

            String ownerId = event.getAppointment().getOwner().getIdDiscord();
            scheduler.schedule(taskFactory.newNotificationTask(embed.build(), ownerId),
                                date);

            for (DiscordUser user : listAttendee) {
                String userId = user.getIdDiscord();
                log.info("setting up notification task for [{}]", userId);
                scheduler.schedule(taskFactory.newNotificationTask(embed.build(), userId),
                    date);
            }
        }
    }
}