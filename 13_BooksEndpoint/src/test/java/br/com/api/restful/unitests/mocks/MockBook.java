package br.com.api.restful.unitests.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.api.restful.data.vo.v1.BookVO;
import br.com.api.restful.model.Book;
import br.com.api.restful.unitests.util.DateFormatter;

public class MockBook {
	
	public Book mockEntity() {
		return mockEntity(0);
	}

	public BookVO mockVO() {
		return mockVO(0);
	}

	public List<Book> mockEntityList() {
		List<Book> books = new ArrayList<Book>();
		for (int i = 0; i < 14; i++) {
			books.add(mockEntity(i));
		}
		return books;
	}

	public List<BookVO> mockVOList() {
		List<BookVO> books = new ArrayList<>();
		for (int i = 0; i < 14; i++) {
			books.add(mockVO(i));
		}
		return books;
	}

	public Book mockEntity(Integer number) {
		Book book = new Book();
		
		book.setId(number);
		book.setAuthor("Author Test" + number);
		book.setTitle("Title Test" + number);
		book.setLaunchDate(DateFormatter.dateFormatter("2022-08-01 00:00"));
		book.setPrice(Double.valueOf("00.00"));
	
		return book;
	}

	public BookVO mockVO(Integer number) {
		BookVO bookVO = new BookVO();
		
		bookVO.setKey(number);
		bookVO.setAuthor("Author Test" + number);
		bookVO.setTitle("Title Test" + number);
		bookVO.setLaunchDate(DateFormatter.dateFormatter("2022-08-01 00:00"));
		bookVO.setPrice(Double.valueOf("00.00"));
		
		return bookVO;
	}
}
