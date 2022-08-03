package br.com.api.restful.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.restful.controllers.BookController;
import br.com.api.restful.data.vo.v1.BookVO;
import br.com.api.restful.exceptions.RequiredObjectIsNullException;
import br.com.api.restful.exceptions.ResourceNotFoundException;
import br.com.api.restful.mapper.DozerMapper;
import br.com.api.restful.model.Book;
import br.com.api.restful.repositories.BookRepository;

@Service
public class BookService {

	private Logger logger = Logger.getLogger(BookService.class.getName());

	@Autowired
	private BookRepository repository;

	public BookVO findById(Integer id) {

		logger.info("Finding one book!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		
		return vo;
	}

	public List<BookVO> findAll() {

		logger.info("Finding all books!");

		var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
		
		books.stream()
			.forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
		
		return books;
	}

	public BookVO create(BookVO booksVO) {
		
		if(booksVO == null) throw new RequiredObjectIsNullException();

		logger.info("Creating one book!");

		var entity = DozerMapper.parseObject(booksVO, Book.class);
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}

	public BookVO update(BookVO booksVO) {
		
		if(booksVO == null) throw new RequiredObjectIsNullException();

		logger.info("Updating one book!");

		var entity = repository.findById(booksVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setAuthor(booksVO.getAuthor());
		entity.setTitle(booksVO.getTitle());
		entity.setLaunchDate(booksVO.getLaunchDate());
		entity.setPrice(booksVO.getPrice());
		
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}

	public void delete(Integer id) {

		logger.info("Deleting one book!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(entity);
	}

}
