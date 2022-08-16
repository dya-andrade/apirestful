package br.com.api.restful.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;

import java.util.List;

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
import br.com.api.restful.integrationtests.asserts.PersonAssertPersisted;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.AccountCredentialsVO;
import br.com.api.restful.integrationtests.vo.PersonVO;
import br.com.api.restful.integrationtests.vo.TokenVO;
import br.com.api.restful.integrationtests.vo.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //port 8888 
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest { //TestContainers

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper; //mapear JSON
	
	private static PersonVO person;
	
	private static PersonAssertPersisted personAssertPersisted;
	
	@BeforeAll //irá inicializar antes dos testes
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); 
		//quando ocorrer falhas por propriedades desconhecidas como HATEAOS
		
		personAssertPersisted = new PersonAssertPersisted();
		
	} //o spring ainda não foi inicializado
	
	@Test
	@Order(0) //Test Auth, simulando POSTMAN
	public void authorization() {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		//antes da specification
		var accessToken = 
				given() //REST Assured
				.basePath("/auth/signin")
					.port(TestConfig.SERVER_PORT)
					.contentType(TestConfig.CONTENT_TYPE_JSON)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
					.extract()
						.body().as(TokenVO.class) //não possui atributos desconhecidos
						.getAccessToken();
		
		//após inicialização do spring para pegar context
		specification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1) //Simulando POSTMAN
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		person = personAssertPersisted.getMockPerson();	
				
		//a specification foi realizada no 0 test	
		
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
		
		personAssertPersisted.assertCreate(persistedPerson);		
	}
	
	@Test
	@Order(2) //Simulando POSTMAN
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {
		
		//a specification foi realizada no 0 test	
		
		var content =
				given() //REST Assured
				.spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
					.pathParams("id", person.getId()) //person está persistido no test 1
				.when()
					.patch("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body().asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		personAssertPersisted.setMockPerson(person);
		
		personAssertPersisted.assertDisable(persistedPerson);		
	}
	
	@Test
	@Order(3) //Simulando POSTMAN
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		//a specification foi realizada no 0 test	
		
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
				
		personAssertPersisted.assertCreate(persistedPerson);		
	}
	
	@Test
	@Order(4) //Simulando POSTMAN
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		person.setLastName("Araújo");
		
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
				.body(person)
				.when()
				.put()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		personAssertPersisted.setMockPerson(person);
		
		personAssertPersisted.assertUpdate(persistedPerson);
	}
	
	@Test
	@Order(5) //Simulando POSTMAN
	public void testDelete() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
				.pathParams("id", person.getId()) //person está persistido no test 1
					.when()
				.delete("{id}")
					.then()
				.statusCode(204);
	}
	
	@Test
	@Order(6) //Simulando POSTMAN
	public void testFindAllWithPageableModel() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
			.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		//List<PersonVO> persistedPeople = objectMapper.readValue(content, new TypeReference<List<PersonVO>>(){});
		/*
			Como está gerando o JSON no Postman
		{
		    "_embedded": {
		        "personVOList": [ ]
		    }
		}
		*/
		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		
		List<PersonVO> persistedPeople = wrapper.getEmbedded().getPersons();
		
		personAssertPersisted.assertList(persistedPeople);		
	}
	
	@Test
	@Order(7) //Simulando POSTMAN
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		//a specification foi realizada no 0 test	
		
		var content =
				given() //REST Assured
				.spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.pathParam("firstName", "ane")				
				.queryParams("page", 0, "size", 5, "direction", "asc")
					.when()
				.get("/findPersonByName/{firstName}")
					.then()
				.statusCode(200)
					.extract()
						.body().asString();
		
		//List<PersonVO> persistedPeople = objectMapper.readValue(content, new TypeReference<List<PersonVO>>(){});
		/*
			Como está gerando o JSON no Postman
		{
		    "_embedded": {
		        "personVOList": [ ]
		    }
		}
		 */
		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		
		List<PersonVO> persistedPeople = wrapper.getEmbedded().getPersons();
		
		personAssertPersisted.assertFindByName(persistedPeople.get(0));		
	}
	
	@Test
	@Order(8) //Simulando POSTMAN
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
				
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given() //REST Assured
			.spec(specificationWithoutToken)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
				.when()
				.get()
			.then()
				.statusCode(403);
	}
	
	@Test
	@Order(9) //Simulando POSTMAN
	public void testHATEAOS() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
			.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		personAssertPersisted.assertHATEAOSJSON(content);
	}
}
