package br.com.api.restful.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.restful.data.vo.v1.PersonVO;
import br.com.api.restful.exceptions.ResourceNotFoundException;
import br.com.api.restful.mapper.DozerMapper;
import br.com.api.restful.model.Person;
import br.com.api.restful.repositories.PersonRepository;

@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());

	@Autowired
	private PersonRepository repository;

	public PersonVO findById(Long id) {

		logger.info("Finding one person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return DozerMapper.parseObject(entity, PersonVO.class);
	}

	public List<PersonVO> findAll() {

		logger.info("Finding all people!");

		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO create(PersonVO personVO) {

		logger.info("Creating one person!");

		var entity = DozerMapper.parseObject(personVO, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

		return vo;
	}

	public PersonVO update(PersonVO personVO) {

		logger.info("Updating one person!");

		var entity = repository.findById(personVO.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		entity.setFirstName(personVO.getFirstName());
		entity.setLastName(personVO.getLastName());
		entity.setAddress(personVO.getAddress());
		entity.setGender(personVO.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

		return vo;
	}

	public void delete(Long id) {

		logger.info("Deleting one person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(entity);
	}

}
