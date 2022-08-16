package br.com.api.restful.integrationtests.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.api.restful.integrationtests.asserts.PersonAssertPersisted;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.PersonVO;
import br.com.api.restful.mapper.DozerMapper;
import br.com.api.restful.model.Person;
import br.com.api.restful.repositories.PersonRepository;

//não precisa do context do spring, apenas do JPA
@ExtendWith(SpringExtension.class)
@DataJpaTest//para testar repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //não irá alterar o bd
@TestMethodOrder(OrderAnnotation.class) 
public class PersonRepositoryTest extends AbstractIntegrationTest { //abstractIntegration cria o banco atraves do flyway
	
	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private static Person person;
	
	private static PersonAssertPersisted personAssertPersisted;
	
	@BeforeAll //irá inicializar antes dos testes
	public static void setup() {
		
		person = new Person();
		
		personAssertPersisted = new PersonAssertPersisted();
		
	} //o spring ainda não foi inicializado
	
	@Test
	@Order(1) //Simulando REPOSITORY
	public void testFindByName()  {
		
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.ASC, "firstName"));
		
		person = repository.findPersonByName("ane", pageable).getContent().get(0);
		
		PersonVO personVO = DozerMapper.parseObject(person, PersonVO.class);

		personAssertPersisted.assertFindByName(personVO);		
		
	}
	
	@Test
	@Order(2) //Simulando REPOSITORY
	public void testDisablePerson()  {
				
		repository.disablePerson(person.getId());
		
		person = repository.findById(person.getId()).get();
		
		PersonVO personVO = DozerMapper.parseObject(person, PersonVO.class);

		personAssertPersisted.setMockPerson(personVO);
		
		personAssertPersisted.assertDisable(personVO);
		
	}

}
