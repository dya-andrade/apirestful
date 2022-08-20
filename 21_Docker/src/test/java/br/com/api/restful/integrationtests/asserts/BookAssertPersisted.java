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

	public void assertCreate(BookVO bookVO) {
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

	public void assertList(List<BookVO> books) {
		BookVO bookVO1 = books.get(1);
		BookVO bookVO2 = books.get(3);
		
		assertNotNull(bookVO1);
		assertNotNull(bookVO1.getId());
		assertNotNull(bookVO1.getTitle());
		assertNotNull(bookVO1.getAuthor());
		assertNotNull(bookVO1.getPrice());
		assertNotNull(bookVO1.getLaunchDate());
		
		assertEquals(4, bookVO1.getId());

		assertEquals("JavaScript", bookVO1.getTitle());
		assertEquals("Crockford", bookVO1.getAuthor());
		assertEquals("67.0", String.valueOf(bookVO1.getPrice()));
		assertEquals("2017-11-07 15:09", dateFormat.format(bookVO1.getLaunchDate()));
		//UTC
		
		assertNotNull(bookVO2);
		assertNotNull(bookVO2.getId());
		assertNotNull(bookVO2.getTitle());
		assertNotNull(bookVO2.getAuthor());
		assertNotNull(bookVO2.getPrice());
		assertNotNull(bookVO2.getLaunchDate());
		
		assertEquals(13, bookVO2.getId());

		assertEquals("O verdadeiro valor de TI", bookVO2.getTitle());
		assertEquals("Richard Hunter e George Westerman", bookVO2.getAuthor());
		assertEquals("95.0", String.valueOf(bookVO2.getPrice()));
		assertEquals("2017-11-07 15:09", dateFormat.format(bookVO2.getLaunchDate()));
		//UTC
	}
	
	public void assertHATEAOSJSON(String content) {
		//quando acessa o context utiliza a porta 8080
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8080/api/book/v1/7\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8080/api/book/v1/15\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8080/api/book/v1/9\"}}}"));

		assertTrue(content.contains("\"page\":{\"size\":3,\"totalElements\":15,\"totalPages\":5,\"number\":2}}"));
		
		assertTrue(content.contains("\"first\":{\"href\":\"http://localhost:8080/api/book/v1?direction=asc&page=0&size=3&sort=title,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8080/api/book/v1?direction=asc&page=1&size=3&sort=title,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8080/api/book/v1?page=2&size=3&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8080/api/book/v1?direction=asc&page=3&size=3&sort=title,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8080/api/book/v1?direction=asc&page=4&size=3&sort=title,asc\"}}"));
	}
	
	public void assertHATEAOSXML(String content) {
		//quando acessa o context utiliza a porta 8080
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8080/api/book/v1/7</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8080/api/book/v1/15</href></links>"));
		assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8080/api/book/v1/9</href></links>"));

		assertTrue(content.contains("<page><size>3</size><totalElements>15</totalElements><totalPages>5</totalPages><number>2</number></page>"));
		
		assertTrue(content.contains("<rel>first</rel><href>http://localhost:8080/api/book/v1?direction=asc&amp;page=0&amp;size=3&amp;sort=title,asc</href>"));
		assertTrue(content.contains("<rel>prev</rel><href>http://localhost:8080/api/book/v1?direction=asc&amp;page=1&amp;size=3&amp;sort=title,asc</href>"));
		assertTrue(content.contains("<rel>self</rel><href>http://localhost:8080/api/book/v1?page=2&amp;size=3&amp;direction=asc</href>"));
		assertTrue(content.contains("<rel>next</rel><href>http://localhost:8080/api/book/v1?direction=asc&amp;page=3&amp;size=3&amp;sort=title,asc</href>"));
		assertTrue(content.contains("<rel>last</rel><href>http://localhost:8080/api/book/v1?direction=asc&amp;page=4&amp;size=3&amp;sort=title,asc</href>"));
	}
	
	public void assertHATEAOSYAML(String content) {
		//quando acessa o context utiliza a porta 8080
		assertTrue(content.contains("links:  - rel: \"self\"    href: \"http://localhost:8080/api/book/v1/15\""));
		assertTrue(content.contains("links:  - rel: \"self\"    href: \"http://localhost:8080/api/book/v1/9\""));
		assertTrue(content.contains("links:  - rel: \"self\"    href: \"http://localhost:8080/api/book/v1/7\""));

		assertTrue(content.contains("page:  size: 3  totalElements: 15  totalPages: 5  number: 2"));
		
		assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8080/api/book/v1?direction=asc&page=0&size=3&sort=title,asc\""));
		assertTrue(content.contains("rel: \"prev\"  href: \"http://localhost:8080/api/book/v1?direction=asc&page=1&size=3&sort=title,asc\""));
		assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8080/api/book/v1?page=2&size=3&direction=asc\""));
		assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8080/api/book/v1?direction=asc&page=3&size=3&sort=title,asc\""));
		assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8080/api/book/v1?direction=asc&page=4&size=3&sort=title,asc\""));
	}
}
