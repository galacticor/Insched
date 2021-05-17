// package com.bot.insched.controller;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.bot.insched.service.GoogleService;
// import com.bot.insched.service.GoogleServiceImpl;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import java.util.Arrays;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(controllers = MainController.class)
// public class MainControllerTest {
// 	@Autowired
// 	MockMvc mvc;

// 	@MockBean
// 	GoogleServiceImpl service;

// 	@BeforeEach
// 	public void setUp(){

// 	}

// 	@Test
// 	public void testHealthCheck() throws Exception {
// 		// mvc.perform(get("/ping").contentType(MediaType.APPLICATION_JSON))
// 		// 		.andExpect(status().isOk());
// 	}
// }