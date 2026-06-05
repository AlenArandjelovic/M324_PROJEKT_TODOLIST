package com.example.demo;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DemoApplication.class)
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void addTaskStoresTaskAndReturnsItInList() throws Exception {
		String description = "MockMvc task " + UUID.randomUUID();

		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"taskdescription\":\"" + description + "\"}"))
				.andExpect(status().isOk())
				.andExpect(content().string("redirect:/"));

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].taskdescription", hasItem(description)));
	}

	@Test
	void deleteTaskRemovesExistingTaskFromList() throws Exception {
		String description = "Task to delete " + UUID.randomUUID();
		String body = "{\"taskdescription\":\"" + description + "\"}";

		mockMvc.perform(post("/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk());

		mockMvc.perform(post("/delete")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk())
				.andExpect(content().string("redirect:/"));

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[*].taskdescription", not(hasItem(description))));
	}

}
