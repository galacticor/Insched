package com.bot.insched.discord.command;

import com.bot.insched.service.GoogleService;
import com.bot.insched.service.GoogleServiceImpl;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthCommandTest{
	// @Mock
 //    User user;

	@Mock
	GoogleService service;

	@Mock
	PrivateMessageReceivedEvent event;

	// @Mock
	// RestAction<PrivateChannel> channel;

	@InjectMocks
	AuthCommand command;

	private String authUrl = "authUrl";

	// @BeforeAll
 //    public static void init() throws Exception {
 //        jda = JDABuilder.createDefault("ODM2NjkzNzYxNjkwMDQyNDA4.YIhtyQ.QlTguqpvUEntyJD0LaQieeQdKvI").build();
 //        jda.retrieveUserById(userId).queue(user -> {
 //            user.openPrivateChannel().queue(privateChannel -> {
 //                privateChannel.retrieveMessageById(messageId).queue(message1 -> {
 //                    message = message1;
 //                });
 //            });
 //        });
 //    }

	@BeforeEach
	public void setUp(){

	}

	// @Test
	// public void testExecute(){
	// 	when(service.getAuthorizationUrl()).thenReturn(authUrl);
	// 	when(event.getAuthor()).thenReturn(user);
	// 	when(user.openPrivateChannel()).thenReturn()
	// 	command.execute(new String[]{"test"}, event);
	// }

}