package br.com.api.restful.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.api.restful.model.Person;
import data.vo.v2.PersonVOV2;

@Service
public class PersonMapper {

	public PersonVOV2 convertEntityToVo(Person person) {
		PersonVOV2 vov2 = new PersonVOV2();
		
		vov2.setId(person.getId());
		vov2.setFirstName(person.getFirstName());
		vov2.setLastName(person.getLastName());
		vov2.setAddress(person.getAddress());
		vov2.setGender(person.getGender());
		vov2.setBirthDay(new Date());
		
		return vov2;
	}
	
	public Person convertVoToEntity(PersonVOV2 personVOV2) {
		Person entity = new Person();
		
		entity.setId(personVOV2.getId());
		entity.setFirstName(personVOV2.getFirstName());
		entity.setLastName(personVOV2.getLastName());
		entity.setAddress(personVOV2.getAddress());
		entity.setGender(personVOV2.getGender());
		
		return entity;
	}

}
