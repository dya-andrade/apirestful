package br.com.api.restful.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.restful.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

}
