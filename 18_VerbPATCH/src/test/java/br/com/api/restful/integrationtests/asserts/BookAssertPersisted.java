package br.com.api.restful.integrationtests.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.api.restful.integrationtests.vo.BookVO;

public class BookAssertPersisted {
	
	private BookVO mockBook;
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public BookAssertPersisted() {
		mockBook = new BookVO();
		
		mockBook.setTitle("Use a cabecao!: Java");
		mockBook.setAuthor("Bert Bates");
		mockBook.setPrice(150.0);
		mockBook.setLaunchDate(new Date());
		
	}

	public BookVO getMockBook() {
		return mockBook;
	}

	public void setMockBook(BookVO mockBook) {
		this.mockBook = mockBook;
	}

	public void assertBook(BookVO bookVO) {
		assertNotNull(bookVO);
		assertNotNull(bookVO.getId());
		assertTrue(bookVO.getId() > 0);
		assertNotNull(bookVO.getTitle());
		assertNotNull(bookVO.getAuthor());
		assertNotNull(bookVO.getPrice());
		assertNotNull(bookVO.getLaunchDate());

		assertEquals(mockBook.getTitle(), bookVO.getTitle());
		assertEquals(mockBook.getAuthor(), bookVO.getAuthor());
		assertEquals(String.valueOf(mockBook.getPrice()), String.valueOf(bookVO.getPrice()));
		assertEquals(new Date(), new Date());		
	}

	public void assertUpdate(BookVO bookVO) {
		assertNotNull(bookVO);
		assertEquals(mockBook.getId(), bookVO.getId());
		assertEquals(mockBook.getTitle(), bookVO.getTitle());		
	}

	public void assertBooksList(List<BookVO> books) {
		BookVO bookVO1 = books.get(1);
		BookVO bookVO2 = books.get(3);
		
		assertNotNull(bookVO1);
		assertNotNull(bookVO1.getId());
		assertNotNull(bookVO1.getTitle());
		assertNotNull(bookVO1.getAuthor());
		assertNotNull(bookVO1.getPrice());
		assertNotNull(bookVO1.getLaunchDate());
		
		assertEquals(2, bookVO1.getId());

		assertEquals("Design Patterns", bookVO1.getTitle());
		assertEquals("Ralph Johnson, Erich Gamma, John Vlissides e Richard Helm", bookVO1.getAuthor());
		assertEquals("45.0", String.valueOf(bookVO1.getPrice()));
		assertEquals("2017-11-29 15:15", dateFormat.format(bookVO1.getLaunchDate()));
		
		assertNotNull(bookVO2);
		assertNotNull(bookVO2.getId());
		assertNotNull(bookVO2.getTitle());
		assertNotNull(bookVO2.getAuthor());
		assertNotNull(bookVO2.getPrice());
		assertNotNull(bookVO2.getLaunchDate());
		
		assertEquals(4, bookVO2.getId());

		assertEquals("JavaScript", bookVO2.getTitle());
		assertEquals("Crockford", bookVO2.getAuthor());
		assertEquals("67.0", String.valueOf(bookVO2.getPrice()));
		assertEquals("2017-11-07 15:09", dateFormat.format(bookVO2.getLaunchDate()));
	}
}
