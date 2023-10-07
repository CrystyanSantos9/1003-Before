package br.com.erudio.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	@Mock
	private PersonRepository repository;

	@InjectMocks
	private PersonServices services;

	private Person person0;

	@BeforeEach
	public void setup() {
		// Given / Arrange
		person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil", "Male");

	}

	@Test
	@DisplayName("JUnit test for Given Person Object when Save Person then Return Person Object")
	void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
		// Given / Arrange
		given(repository.findByEmail(anyString())).willReturn(Optional.empty());
		given(repository.save(person0)).willReturn(person0);

		// When / Act
		Person savedPerson = services.create(person0);

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals("Leandro", savedPerson.getFirstName());

	}

	@Test
	@DisplayName("JUnit test for Given Existin Email when Save Person then throws Exception")
	void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {
		// Given / Arrange
		given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));

		// When / Act
		assertThrows(ResourceNotFoundException.class, () -> {
			services.create(person0);
		});

		// Then / Assert
		verify(repository, never()).save(any(Person.class));

	}

	@Test
	@DisplayName("JUnit test for Given Persons List when findAll Persons then Return Persons List")
	void testGivenPersonsList_WhenFindAll_thenReturnPersonsList() {
		// Given / Arrange
		Person person1 = new Person("Jaqueline", "Costa", "jaqueline@erudio.com.br",
				"Uberlândia - Minas Gerais - Brasil", "Female");

		given(repository.findAll()).willReturn(List.of(person0, person1));

		// When / Act
		List<Person> personsList = services.findAll();

		// Then / Assert
		assertNotNull(personsList);
		assertEquals(2, personsList.size());

	}

	@Test
	@DisplayName("JUnit test for Given PersonId when findById then Return Person Object")
	void testGivenPersonId_WhenFindById_thenReturnPersonObject() {
		// Given / Arrange
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));

		// When / Act
		Person savedPerson = services.findById(1L);

		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals("Leandro", savedPerson.getFirstName());

	}

	@Test
	@DisplayName("JUnit test for Given Person Object when Update then Return Updated Person Object")
	void testGivenPersonObject_WhenUpdate_thenReturnUpdatedPersonObject() {
		// Given / Arrange
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		
		person0.setFirstName("Angelica");
		person0.setEmail("angelica@vovo.com");
		
		given(repository.save(person0)).willReturn(person0);

		// When / Act
		Person updatedPerson = services.update(person0);

		// Then / Assert
		assertNotNull(updatedPerson);
		assertEquals("Angelica", updatedPerson.getFirstName());
		assertEquals("angelica@vovo.com", updatedPerson.getEmail());

	}

	@Test
	@DisplayName("JUnit test for Given Person Id when Delete Person then Do Nothing")
	void testGivenPersonID_WhenDeletePerson_thenDoNothing() {
		// Given / Arrange
		person0.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person0));
		willDoNothing().given(repository).delete(person0);

		// When / Act
		services.delete(person0.getId());

		// Then / Assert
		verify(repository, times(1)).delete(person0);

	}
}
