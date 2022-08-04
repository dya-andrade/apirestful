package br.com.api.restful.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api.restful.configs.TestConfig;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.PersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest { //TestContainers

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static PersonVO person;
	
	@BeforeAll //irá inicializar antes dos testes
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); 
		//quando ocorrer falhas por propriedades desconhecidas como HATEAOS
		
		person = new PersonVO();
		
	} //o spring ainda não foi inicializado
	
	
	@Test
	@Order(1) //Test CORS, simulando POSTMAN
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		//após inicialização do spring para pegar context
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_APIRESTFUL)
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
				.body(person)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertTrue(persistedPerson.getId() > 0);
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertEquals("Dyane", persistedPerson.getFirstName());
		assertEquals("Andrade", persistedPerson.getLastName());
		assertEquals("Embu das Artes - SP", persistedPerson.getAddress());
		assertEquals("Female", persistedPerson.getGender());

	}
	
	
	@Test
	@Order(2) //Test CORS, simulando POSTMAN
	public void testCreateWtihWrongOrigin() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		//após inicialização do spring para pegar context
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_WRONG)
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content =
				given() //REST Assured
				.spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(403)
				.extract()
					.body().asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
		
	}
	

	@Test
	@Order(3) //Test CORS, simulando POSTMAN
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		//após inicialização do spring para pegar context
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_APIRESTFUL)
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
				.pathParams("id", person.getId()) //person está persistido no test 1
				.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertTrue(persistedPerson.getId() > 0);
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertEquals("Dyane", persistedPerson.getFirstName());
		assertEquals("Andrade", persistedPerson.getLastName());
		assertEquals("Embu das Artes - SP", persistedPerson.getAddress());
		assertEquals("Female", persistedPerson.getGender());

	}
	
	
	@Test
	@Order(4) //Test CORS, simulando POSTMAN
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		
		mockPerson();
		
		//após inicialização do spring para pegar context
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_WRONG)
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
				.pathParams("id", person.getId()) //person está persistido no test 1
				.when()
				.get("{id}")
			.then()
				.statusCode(403)
			.extract()
				.body().asString();
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);

	}

	private void mockPerson() {
		person.setFirstName("Dyane");
		person.setLastName("Andrade");
		person.setAddress("Embu das Artes - SP");
		person.setGender("Female");
	}

}
