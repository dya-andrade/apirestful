package br.com.api.restful.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.api.restful.controllers.PersonController;
import br.com.api.restful.data.vo.v1.PersonVO;
import br.com.api.restful.exceptions.RequiredObjectIsNullException;
import br.com.api.restful.exceptions.ResourceNotFoundException;
import br.com.api.restful.mapper.DozerMapper;
import br.com.api.restful.model.Person;
import br.com.api.restful.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());

	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private PagedResourcesAssembler<PersonVO> assembler; //montadora
	

	public PersonVO findById(Long id) {

		logger.info("Finding one person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return vo;
	}

	public List<PersonVO> findAll() {

		logger.info("Finding all people!");

		var personsVOs = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		
		personsVOs.stream()
			.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		return personsVOs;
	}
	
	public Page<PersonVO> findAll(Pageable pageable) {

		logger.info("Finding all people with pageable!");
		
		var personPage = repository.findAll(pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class)); //converter person entity para VO

		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())); //add links HATEOAS
		
		return personVOsPage;
	}
	
	public PagedModel<EntityModel<PersonVO>> findAllWithPagedModel(Pageable pageable) {

		logger.info("Finding all people with pageable model!");
		
		var personPage = repository.findAll(pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class)); //converter person entity para VO

		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())); //add links HATEOAS
		
		Link link = linkTo(methodOn(PersonController.class)
				.findAllWithPageableModel(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel(); //links das pages HATEAOS
		
		return assembler.toModel(personVOsPage, link); 
	}
	
	public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
		
		logger.info("Finding people by first name with pageable model!");
		
		var personPage = repository.findPersonByName(firstName, pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class)); //converter person entity para VO
		
		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())); //add links HATEOAS
		
		Link link = linkTo(methodOn(PersonController.class)
				.findAllWithPageableModel(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel(); //links das pages HATEAOS
		
		return assembler.toModel(personVOsPage, link); 
	}

	public PersonVO create(PersonVO personVO) {
		
		if(personVO == null) throw new RequiredObjectIsNullException();

		logger.info("Creating one person!");

		var entity = DozerMapper.parseObject(personVO, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}

	public PersonVO update(PersonVO personVO) {
		
		if(personVO == null) throw new RequiredObjectIsNullException();

		logger.info("Updating one person!");

		var entity = repository.findById(personVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		entity.setFirstName(personVO.getFirstName());
		entity.setLastName(personVO.getLastName());
		entity.setAddress(personVO.getAddress());
		entity.setGender(personVO.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

		
		return vo;
	}

	//“tudo ou nada” (princípio da atomicidade)
	//https://thorben-janssen.com/transactions-spring-data-jpa/
	//https://www.devmedia.com.br/conheca-o-spring-transactional-annotations/32472
	@Transactional
	public PersonVO disablePerson(Long id) {

		logger.info("Disabling one person!");
		
		repository.disablePerson(id);

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return vo;
	}
	
	public void delete(Long id) {

		logger.info("Deleting one person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(entity);
	}
}
