package com.bot.insched.discord.receiver;

import com.bot.insched.discord.command.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Receiver extends ListenerAdapter {

    private final Map<String, Command> commands = new HashMap<>();

    public Receiver() {
        addCommand(new HelloCommand());
        addCommand(new BookAppointmentCommand());
        addCommand(new CreateEventCommand());
        addCommand(new CreateAppointmentCommand());
        addCommand(new ShowCalendarCommand());
        addCommand(new HelpCommand());
        addCommand(new errorCommand());
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

        String comm = message.split(" ")[0].replace("!","");
        String[] msg = message.split(" ");
        String[] args = Arrays.copyOfRange(msg, 1,msg.length);
        if (commands.containsKey(comm)) {
            getCommand(comm).execute(args, event);
        }else{
            getCommand("error").execute(args,event);
        }
    }

}
