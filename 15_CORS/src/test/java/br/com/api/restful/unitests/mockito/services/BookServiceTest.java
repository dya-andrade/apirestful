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

import br.com.api.restful.data.vo.v1.BookVO;
import br.com.api.restful.exceptions.RequiredObjectIsNullException;
import br.com.api.restful.model.Book;
import br.com.api.restful.repositories.BookRepository;
import br.com.api.restful.services.BookService;
import br.com.api.restful.unitests.mocks.MockBook;
import br.com.api.restful.unitests.util.DateFormatter;

@TestInstance(Lifecycle.PER_CLASS) // Instância por class
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	MockBook input;

	@InjectMocks
	private BookService service;

	@Mock
	private BookRepository repository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this); // set mocks em PersonServiceTest
	}

	private Book findEntity(Integer id) {
		Book entity = input.mockEntity(id);
		entity.setId(id);

		when(repository.findById(id)).thenReturn(Optional.of(entity));

		return entity;
	}

	private BookVO persistEntity(Book entity) {
		Book persisted = entity;
		persisted.setId(entity.getId());

		BookVO vo = input.mockVO(1);
		vo.setKey(entity.getId());

		when(repository.save(entity)).thenReturn(persisted);

		return vo;
	}

	private void assertResult(BookVO result) {
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());

		int numero = result.getKey();

		assertTrue(result.toString().contains("links: [</api/book/v1/" + numero + ">;rel=\"self\"]"));

		
		assertEquals("Author Test" + numero, result.getAuthor());
		assertEquals("Title Test" + numero, result.getTitle());
		assertEquals("2022-08-01 00:00", DateFormatter.dateInString(result.getLaunchDate()));
		assertEquals("0.0", String.valueOf(result.getPrice()));
	}

	private void assertExceptionIsNull(Exception exception) {
		String exceptedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(exceptedMessage));
	}

	@Test
	void testFindById() {
		Book entity = findEntity(1);

		var result = service.findById(entity.getId());

		assertResult(result);
	}

	@Test
	void testFindAll() {
		List<Book> list = input.mockEntityList();

		when(repository.findAll()).thenReturn(list);

		var result = service.findAll();

		assertNotNull(result);
		assertEquals(14, result.size());

		var bookOne = result.get(1);

		assertResult(bookOne);

		var bookFour = result.get(4);

		assertResult(bookFour);

		var bookSeven = result.get(7);

		assertResult(bookSeven);
	}

	@Test
	void testCreate() {
		Book entity = input.mockEntity(1);
		BookVO vo = persistEntity(entity);

		var result = service.create(vo);

		assertResult(result);
	}

	@Test
	void testCreateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> { // lambda
			service.create(null);
		});

		assertExceptionIsNull(exception);
	}

	@Test
	void testUpdate() {
		Book entity = findEntity(1);
		BookVO vo = persistEntity(entity);

		var result = service.update(vo);

		assertResult(result);
	}

	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> { // lambda
			service.update(null);
		});

		assertExceptionIsNull(exception);
	}

	@Test
	void testDelete() {
		Book entity = findEntity(1);

		service.delete(entity.getId());
	}
}
