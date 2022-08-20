package br.com.api.restful.unitests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.api.restful.data.vo.v1.PersonVO;
import br.com.api.restful.exceptions.RequiredObjectIsNullException;
import br.com.api.restful.model.Person;
import br.com.api.restful.repositories.PersonRepository;
import br.com.api.restful.services.PersonService;
import br.com.api.restful.unitests.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS) //  cria apenas uma instância da classe de teste e reutilizá-la entre os testes.
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

	MockPerson input;

	@InjectMocks
	private PersonService service;

	@Mock
	private PersonRepository repository;

	@BeforeEach //executado antes de cada método 
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this); // set mocks em PersonServiceTest
	}

	private Person findEntity(Long id) {
		Person entity = input.mockEntity(id.intValue());
		entity.setId(id);

		when(repository.findById(id)).thenReturn(Optional.of(entity));

		return entity;
	}

//	private PersonVO persistEntity(Person entity) {
//		Person persisted = entity;
//		persisted.setId(entity.getId());
//
//		PersonVO vo = input.mockVO(1);
//		vo.setKey(entity.getId());
//
//		when(repository.save(entity)).thenReturn(persisted);
//
//		return vo;
//	}

	private void assertResult(PersonVO result) {
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		int numero = result.getKey().intValue();

		assertTrue(result.toString().contains("links: [</api/person/v1/" + numero + ">;rel=\"self\"]"));
		
		assertEquals("First Name Test" + numero, result.getFirstName());
		assertEquals("Last Name Test" + numero, result.getLastName());
		assertEquals("Address Test" + numero, result.getAddress());

		if (numero % 2 == 0) {
			assertEquals("Male", result.getGender());
		} else {
			assertEquals("Female", result.getGender());
		}
	}

	private void assertExceptionIsNull(Exception exception) {
		String exceptedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(exceptedMessage));
	}

	@Test
	void testFindById() {
		Person entity = findEntity(1L);

		var result = service.findById(entity.getId());

		assertResult(result);
	}

	@Test
	void testFindAll() {
		List<Person> list = input.mockEntityList();

		when(repository.findAll()).thenReturn(list);

		var result = service.findAll();

		assertNotNull(result);
		assertEquals(14, result.size());

		var personOne = result.get(1);

		assertResult(personOne);

		var personFour = result.get(4);

		assertResult(personFour);

		var personSeven = result.get(7);

		assertResult(personSeven);
	}

//	@Test
//	void testCreate() {
//		Person entity = input.mockEntity(1);
//		PersonVO vo = persistEntity(entity);
//
//		var result = service.create(vo);
//
//		assertResult(result);
//	}

	@Test
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> { // lambda
			service.create(null);
		});

		assertExceptionIsNull(exception);
	}

//	@Test
//	void testUpdate() {
//		Person entity = findEntity(1L);
//		PersonVO vo = persistEntity(entity);
//
//		var result = service.update(vo);
//
//		assertResult(result);
//	}

	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> { // lambda
			service.update(null);
		});

		assertExceptionIsNull(exception);
	}

	@Test
	void testDelete() {
		Person entity = findEntity(1L);

		service.delete(entity.getId());
	}

}
