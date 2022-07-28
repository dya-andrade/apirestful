package br.com.api.restful.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.restful.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
