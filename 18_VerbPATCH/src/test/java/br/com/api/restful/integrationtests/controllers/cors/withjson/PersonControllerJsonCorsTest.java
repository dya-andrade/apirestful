package br.com.api.restful.integrationtests.controllers.cors.withjson;

import static io.restassured.RestAssured.given;

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
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //port 8888 
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonCorsTest extends AbstractIntegrationTest { //TestContainers

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
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
	@Order(1) //Test CORS, simulando POSTMAN
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		
		person = personAssertPersisted.getMockPerson();	
		
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
			.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_APIRESTFUL)
				.body(person)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
				
		personAssertPersisted.assertPerson(persistedPerson);		
	}
	
	
	@Test
	@Order(2) //Test CORS, simulando POSTMAN
	public void testCreateWtihWrongOrigin() throws JsonMappingException, JsonProcessingException {

		//a specification foi realizada no 0 test	
		
		var content =
				given() //REST Assured
				.spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_WRONG)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(403)
				.extract()
					.body().asString();
		
		personAssertPersisted.assertWrongOrigin(content);
	}
	

	@Test
	@Order(3) //Test CORS, simulando POSTMAN
	public void testFindById() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
			.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_APIRESTFUL)
				.pathParams("id", person.getId()) //person está persistido no test 1
				.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		personAssertPersisted.assertPerson(persistedPerson);		
	}
	
	
	@Test
	@Order(4) //Test CORS, simulando POSTMAN
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
			.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_URL_WRONG)
				.pathParams("id", person.getId()) //person está persistido no test 1
				.when()
				.get("{id}")
			.then()
				.statusCode(403)
			.extract()
				.body().asString();
		
		personAssertPersisted.assertWrongOrigin(content);		
	}
}
