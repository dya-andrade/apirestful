package br.com.api.restful.integrationtests.asserts;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import br.com.api.restful.integrationtests.vo.PersonVO;

public class PersonAssertPersisted {
	
	private PersonVO mockPerson;
	
	public PersonAssertPersisted() {
		mockPerson = new PersonVO();
		
		mockPerson.setFirstName("Dyane");
		mockPerson.setLastName("Andrade");
		mockPerson.setAddress("Embu das Artes - SP");
		mockPerson.setGender("Female");
		mockPerson.setEnabled(true);
	}
	
	public PersonVO getMockPerson() {
		return mockPerson;
	}

	public void setMockPerson(PersonVO personVO) {
		this.mockPerson = personVO;
	}

	public void assertCreate(PersonVO personVO) {

		assertPerson(personVO);
		
		if(mockPerson.getEnabled()) 
			assertTrue(personVO.getEnabled());
		else
			assertFalse(personVO.getEnabled());
	}
	
	private void assertPerson(PersonVO personVO) {

		assertNotNull(personVO);
		assertNotNull(personVO.getId());
		assertTrue(personVO.getId() > 0);
		assertNotNull(personVO.getFirstName());
		assertNotNull(personVO.getLastName());
		assertNotNull(personVO.getAddress());
		assertNotNull(personVO.getGender());

		assertEquals(mockPerson.getFirstName(), personVO.getFirstName());
		assertEquals(mockPerson.getLastName(), personVO.getLastName());
		assertEquals(mockPerson.getAddress(), personVO.getAddress());
		assertEquals(mockPerson.getGender(), personVO.getGender());
	}
	
	public void assertDisable(PersonVO personVO) {
		assertPerson(personVO);
		assertFalse(personVO.getEnabled());	
	}

	public void assertList(List<PersonVO> people) {
		PersonVO personVO1 = people.get(1);
		PersonVO personVO2 = people.get(3);
		
		assertNotNull(personVO1);
		assertNotNull(personVO1.getId());
		assertNotNull(personVO1.getFirstName());
		assertNotNull(personVO1.getLastName());
		assertNotNull(personVO1.getAddress());
		assertNotNull(personVO1.getGender());
		
		assertFalse(personVO1.getEnabled());
		
		assertEquals(943, personVO1.getId());

		assertEquals("Alleen", personVO1.getFirstName());
		assertEquals("Bunson", personVO1.getLastName());
		assertEquals("5173 Little Fleur Point", personVO1.getAddress());
		assertEquals("Female", personVO1.getGender());
		
		assertNotNull(personVO2);
		assertNotNull(personVO2.getId());
		assertNotNull(personVO2.getFirstName());
		assertNotNull(personVO2.getLastName());
		assertNotNull(personVO2.getAddress());
		assertNotNull(personVO2.getGender());
		
		assertTrue(personVO2.getEnabled());
		
		assertEquals(582, personVO2.getId());

		assertEquals("Alleyn", personVO2.getFirstName());
		assertEquals("Lebourn", personVO2.getLastName());
		assertEquals("3525 Manufacturers Plaza", personVO2.getAddress());
		assertEquals("Male", personVO2.getGender());		
	}
	
	public void assertFindByName(PersonVO personVO) {
		assertNotNull(personVO);
		assertNotNull(personVO.getId());
		assertTrue(personVO.getId() > 0);
		assertNotNull(personVO.getFirstName());
		assertNotNull(personVO.getLastName());
		assertNotNull(personVO.getAddress());
		assertNotNull(personVO.getGender());
		
		assertEquals(947, personVO.getId());
		
		assertFalse(personVO.getEnabled());

		assertEquals("Anestassia", personVO.getFirstName());
		assertEquals("Purton", personVO.getLastName());
		assertEquals("479 Killdeer Court", personVO.getAddress());
		assertEquals("Female", personVO.getGender());
	}

	public void assertUpdate(PersonVO personVO) {
		assertNotNull(personVO);
		assertEquals(mockPerson.getId(), personVO.getId());
		assertEquals(mockPerson.getLastName(), personVO.getLastName());		
	}
	
	public void assertWrongOrigin(String content) {
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}
}
