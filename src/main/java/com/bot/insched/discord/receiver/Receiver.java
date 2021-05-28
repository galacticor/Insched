package com.bot.insched.discord.receiver;

import com.bot.insched.discord.command.*;
import com.bot.insched.service.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final Map<String, Command> commands = new HashMap<>();

    @Autowired
    public Receiver(
            GoogleService googleService,
            AppointmentService appointmentService,
            DiscordUserService discordUserService,
            EventService eventService,
            BookingAppointmentService bookingAppointmentService,
            ShowCalendarService showCalendarService
    ) {
        addCommand(new HelloCommand(googleService));
        addCommand(new BookAppointmentCommand());
        addCommand(new CreateEventCommand(eventService, discordUserService));
        addCommand(new UpdateEventCommand(eventService, discordUserService));
        addCommand(new DeleteEventCommand(eventService, discordUserService));
        addCommand(new HelpCommand());
        addCommand(new LoginCommand(googleService));
        addCommand(new LogoutCommand(googleService, discordUserService));
        addCommand(new AuthCommand(googleService));
        addCommand(new ErrorCommand());
        addCommand(new MyTokenCommand(appointmentService, discordUserService));
        addCommand(new CreateSlotCommand(appointmentService));
        addCommand(new MyAppointmentListCommand(appointmentService));
        addCommand(new ShowCalendarCommand(showCalendarService));
        addCommand(new EditSlotCommand(appointmentService));
        addCommand(new DeleteSlotCommand(appointmentService));
    }

    private void addCommand(Command command) {
        if (command != null && !commands.containsKey(command.getCommand())) {
            commands.put(command.getCommand(), command);
        }
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public void execute(PrivateMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (!message.startsWith("!")) {
            return;
        }

        String comm = message.split(" ")[0].replace("!", "");
        String[] msg = message.split(" ");
        String[] args = Arrays.copyOfRange(msg, 1, msg.length);
        if (commands.containsKey(comm)) {
            getCommand(comm).execute(args, event);
        } else {
            getCommand("error").execute(args, event);
        }
    }

}
