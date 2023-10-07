package br.com.erudio.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.services.PersonServices;

@WebMvcTest
public class PersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PersonServices service;

	private Person person;

	@BeforeEach
	public void setup() {
		// Given / Arrange
		person = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil", "Male");

	}

	@Test
	@DisplayName("JUnit test for Given List of Persons when findAll Persons then Return Persons List")
	void testGivenPersonsList_When_findAll_ThenReturnPersonsList() throws JsonProcessingException, Exception {
		// Given / Arrange
		List<Person> persons = new ArrayList<>();
		persons.add(person);

		persons.add(
				new Person("Rafael", "Costa", "Rafael@erudio.com.br", "Uberlândia - Minas Gerais - Brasil", "Male"));

		given(service.findAll()).willReturn(persons);

		// When / Act
		ResultActions response = mockMvc.perform(get("/person"));

		// Then / Assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()", is(persons.size())));

	}

	@Test
	@DisplayName("JUnit test for Given PersonId when findById then Return Person Object")
	void testGivenPersonId_When_findById_ThenReturnPersonObject() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;

		given(service.findById(personId)).willReturn(person);

		// When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", personId));

		// Then / Assert
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is(person.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(person.getLastName())))
				.andExpect(jsonPath("$.email", is(person.getEmail())));

	}

	@Test
	@DisplayName("JUnit test for Given Updated Person when Update then Return Updated Person Object")
	void testGivenUpdatedPerson_When_Updated_ThenReturnUpdatedPersonObject() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;
		given(service.findById(personId)).willReturn(person);
		given(service.update(any())).willAnswer((invocation) -> invocation.getArgument(0));

		// When / Act
		Person updatedPerson = new Person("Rafael", "Costa", "Rafael@erudio.com.br",
				"Uberlândia - Minas Gerais - Brasil", "Male");

		ResultActions response = mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPerson)));

		// Then / Assert
		response.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));

	}

	@Test
	@DisplayName("JUnit test for Given Unexistent Person when Update then Return Not Found")
	void testGivenUnexistentPerson_When_Updated_ThenReturnNotFoun() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;
		given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
		given(service.update(any())).willAnswer((invocation) -> invocation.getArgument(1));

		// When / Act
		Person updatedPerson = new Person("Rafael", "Costa", "Rafael@erudio.com.br",
				"Uberlândia - Minas Gerais - Brasil", "Male");

		ResultActions response = mockMvc.perform(put("/person").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPerson)));

		// Then / Assert
		response.andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	@DisplayName("JUnit test for Given PersonId when Delete then Return No Content")
	void testGivenPersonId_When_Delete_ThenReturnNoContent() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;
		willDoNothing().given(service).delete(personId);

		// When / Act

		ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

		// Then / Assert
		response.andExpect(status().isNoContent()).andDo(print());

	}

}
