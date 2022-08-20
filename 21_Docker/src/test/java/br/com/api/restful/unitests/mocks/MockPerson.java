package br.com.api.restful.unitests.mocks;

import java.util.ArrayList;
import java.util.List;

import br.com.api.restful.data.vo.v1.PersonVO;
import br.com.api.restful.model.Person;

public class MockPerson {
	public Person mockEntity() {
		return mockEntity(0);
	}

	public PersonVO mockVO() {
		return mockVO(0);
	}

	public List<Person> mockEntityList() {
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < 14; i++) {
			persons.add(mockEntity(i));
		}
		return persons;
	}

	public List<PersonVO> mockVOList() {
		List<PersonVO> persons = new ArrayList<>();
		for (int i = 0; i < 14; i++) {
			persons.add(mockVO(i));
		}
		return persons;
	}

	public Person mockEntity(Integer number) {
		Person person = new Person();
		
		person.setId(number.longValue());
		person.setFirstName("First Name Test" + number);
		person.setLastName("Last Name Test" + number);
		person.setAddress("Address Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		
		return person;
	}

	public PersonVO mockVO(Integer number) {
		PersonVO person = new PersonVO();
		
		person.setKey(number.longValue());
		person.setFirstName("First Name Test" + number);
		person.setLastName("Last Name Test" + number);
		person.setAddress("Address Test" + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		
		return person;
	}
}
