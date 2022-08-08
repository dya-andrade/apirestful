package br.com.api.restful.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.api.restful.configs.TestConfig;
import br.com.api.restful.integrationtests.asserts.PersonAssertPersisted;
import br.com.api.restful.integrationtests.controllers.withyaml.mapper.YMLMapper;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.AccountCredentialsVO;
import br.com.api.restful.integrationtests.vo.PersonVO;
import br.com.api.restful.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //port 8888 
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest { //TestContainers

	private static RequestSpecification specification;
	private static YMLMapper ymlMapper; //mapear XML
	
	private static PersonVO person;
	
	private static PersonAssertPersisted personAssertPersisted;
	
	@BeforeAll //irá inicializar antes dos testes
	public static void setup() {
		ymlMapper = new YMLMapper();
		//ymlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); 
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
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfig.SERVER_PORT)
					.contentType(TestConfig.CONTENT_TYPE_YML) //receber
					.accept(TestConfig.CONTENT_TYPE_YML) //retornar
				.body(user, ymlMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
					.extract()
						.body().as(TokenVO.class, ymlMapper) //não possui atributos desconhecidos
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
		
		var persistedPerson =
			given() //REST Assured
			.spec(specification)
			.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfig.CONTENT_TYPE_YML) //receber
			.accept(TestConfig.CONTENT_TYPE_YML) //retornar
				.body(person, ymlMapper)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body().as(PersonVO.class, ymlMapper);
		
		person = persistedPerson;
		
		personAssertPersisted.assertPerson(persistedPerson);		
	}
	
	@Test
	@Order(2) //Simulando POSTMAN
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		//a specification foi realizada no 0 test	
		
		var persistedPerson =
			given() //REST Assured
			.spec(specification)
			.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfig.CONTENT_TYPE_YML) //receber
			.accept(TestConfig.CONTENT_TYPE_YML) //retornar
				.pathParams("id", person.getId()) //person está persistido no test 1
				.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body().as(PersonVO.class, ymlMapper);
		
		person = persistedPerson;
		
		personAssertPersisted.assertPerson(persistedPerson);		
	}
	
	@Test
	@Order(3) //Simulando POSTMAN
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		person.setLastName("Araújo");
		
		//a specification foi realizada no 0 test	
		
		var persistedPerson =
			given() //REST Assured
			.spec(specification)
			.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfig.CONTENT_TYPE_YML) //receber
			.accept(TestConfig.CONTENT_TYPE_YML) //retornar
				.body(person, ymlMapper)
				.when()
				.put()
			.then()
				.statusCode(200)
			.extract()
				.body().as(PersonVO.class, ymlMapper);
		
		person = persistedPerson;
		
		personAssertPersisted.setMockPerson(person);
		
		personAssertPersisted.assertUpdate(persistedPerson);		
	}
	
	@Test
	@Order(4) //Simulando POSTMAN
	public void testDelete() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		given() //REST Assured
			.spec(specification)
			.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfig.CONTENT_TYPE_YML) //receber
				.pathParams("id", person.getId()) //person está persistido no test 1
					.when()
				.delete("{id}")
					.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5) //Simulando POSTMAN
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfig.CONTENT_TYPE_YML) //receber 
			.accept(TestConfig.CONTENT_TYPE_YML) //retornar
				.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body().as(PersonVO[].class, ymlMapper);
		
		List<PersonVO> persistedPeople = Arrays.asList(content);
		
		personAssertPersisted.assertPeopleList(persistedPeople);
	}
	
	@Test
	@Order(6) //Simulando POSTMAN
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
				
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given() //REST Assured
			.spec(specificationWithoutToken)
			.config(RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfig.CONTENT_TYPE_YML) //receber			
				.when()
				.get()
			.then()
				.statusCode(403);
	}
}
