package br.com.erudio.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.erudio.model.Person;

@DataJpaTest
class PersonRepositoryTest {

	@Autowired
	private PersonRepository personRepository;

	@Test
	@DisplayName("Given Person Object when Save then Return Saved Person")
	void testGivenPersonObject_When_Save_thenReturnSavedPerson() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");

		// When / Act
		Person savedPerson = personRepository.save(person0);
		
		// Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
	}
	
	@Test
	@DisplayName("Given Person Object when findaAll then Return List Person")
	void testGivenPersonObject_When_findAll_thenReturnPersonList() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		Person person1 = new Person("Carlos", "Costa", "carlos@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		personRepository.save(person0);
		personRepository.save(person1);

		// When / Act
		List<Person> personList = personRepository.findAll();
		
		// Then / Assert
		assertNotNull(personList);
		assertEquals(2, personList.size());
	}
	
	@Test
	@DisplayName("Given Person Object when FindById then Return Person Object")
	void testGivenPersonObject_When_FindById_thenReturnPersonObject() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		personRepository.save(person0);
	
		// When / Act
		Person savedPerson = personRepository.findById(person0.getId()).get();
		
		
		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(person0.getId(), savedPerson.getId());
	}
	
	@Test
	@DisplayName("Given Person Object when Update Person then Return Updated Person Object")
	void testGivenPersonObject_When_UpdatePerson_thenReturnUpdatedPersonObject() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		personRepository.save(person0);
	
		// When / Act
		Person savedPerson = personRepository.findById(person0.getId()).get();
		savedPerson.setFirstName("Gabriel");
		savedPerson.setEmail("gabriel@erudio.com.br");
		
		Person updatedPerson = personRepository.save(savedPerson);
		
		// Then / Assert
		assertNotNull(updatedPerson);
		assertEquals("Gabriel", updatedPerson.getFirstName());
		assertEquals("gabriel@erudio.com.br", updatedPerson.getEmail());
	}
	
	@Test
	@DisplayName("Given Person Object when Delete Person then Remove Person")
	void testGivenPersonObject_When_DeletePerson_thenRemovePersonObject() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		personRepository.save(person0);
	
		// When / Act
		personRepository.deleteById(person0.getId());
		
		Optional<Person> personOptional = personRepository.findById(person0.getId());
		
		// Then / Assert
		assertTrue(personOptional.isEmpty());
	}
	
	@Test
	@DisplayName("Given FirstName And LastName Object when findByJPQLNamedParameters then Return Person Object")
	void testGivenFirstNameAndLastNameObject_When_findByJPQLNamedParameters_thenReturnPersonObject() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		personRepository.save(person0);
	
		String firstName = "Leandro";
		String lastName = "Costa";
		
		// When / Act
	    Person savedPerson = personRepository.findByJPQLNamedParameters(firstName, lastName);
		
		// Then / Assert
	    assertNotNull(savedPerson);
	    assertEquals(firstName, savedPerson.getFirstName());
	    assertEquals(lastName, savedPerson.getLastName());
	}
	
	@Test
	@DisplayName("Given FirstName And LastName Object when findByNativeSQL then Return Person Object")
	void testGivenFirstNameAndLastNameObject_When_findByNativeSQL_thenReturnPersonObject() {
		// Given / Arrange
		Person person0 = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil",
				"Male");
		
		personRepository.save(person0);
	
		String firstName = "Leandro";
		String lastName = "Costa";
		
		// When / Act
	    Person savedPerson = personRepository.findByNativeSQL(firstName, lastName);
		
		// Then / Assert
	    assertNotNull(savedPerson);
	    assertEquals(firstName, savedPerson.getFirstName());
	    assertEquals(lastName, savedPerson.getLastName());
	}
}
