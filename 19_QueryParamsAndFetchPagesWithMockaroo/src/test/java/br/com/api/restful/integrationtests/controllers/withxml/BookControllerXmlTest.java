package br.com.api.restful.integrationtests.controllers.withxml;

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
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.api.restful.configs.TestConfig;
import br.com.api.restful.integrationtests.asserts.BookAssertPersisted;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.AccountCredentialsVO;
import br.com.api.restful.integrationtests.vo.BookVO;
import br.com.api.restful.integrationtests.vo.TokenVO;
import br.com.api.restful.integrationtests.vo.pagedmodels.PagedModelBookVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //port 8888 
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest { //TestContainers

	private static RequestSpecification specification;
	private static XmlMapper xmlMapper; //mapear XML
	
	private static BookVO book;
	
	private static BookAssertPersisted bookAssertPersisted;
	
	@BeforeAll //irá inicializar antes dos testes
	public static void setup() {
		xmlMapper = new XmlMapper();
		xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); 
		//quando ocorrer falhas por propriedades desconhecidas como HATEAOS
				
		bookAssertPersisted = new BookAssertPersisted();
				
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
					.contentType(TestConfig.CONTENT_TYPE_XML) //receber
					.accept(TestConfig.CONTENT_TYPE_XML) //retornar
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
				.setBasePath("/api/book/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1) //Simulando POSTMAN
	public void testCreate() throws JsonMappingException, JsonProcessingException {
				
		book = bookAssertPersisted.getMockBook();	
		
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_XML) //receber
			.accept(TestConfig.CONTENT_TYPE_XML) //retornar
				.body(book)
				.when()
				.post()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		BookVO persistedBook = xmlMapper.readValue(content, BookVO.class);
		book = persistedBook;
						
		bookAssertPersisted.assertCreate(persistedBook);		
	}
	
	@Test
	@Order(2) //Simulando POSTMAN
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_XML) //receber
			.accept(TestConfig.CONTENT_TYPE_XML) //retornar
				.pathParams("id", book.getId()) //person está persistido no test 1
				.when()
				.get("{id}")
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		BookVO persistedBook = xmlMapper.readValue(content, BookVO.class);
		book = persistedBook;
		
		bookAssertPersisted.assertCreate(persistedBook);		
	}
	
	@Test
	@Order(3) //Simulando POSTMAN
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		book.setTitle("Clean Code");;
		
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_XML) //receber
			.accept(TestConfig.CONTENT_TYPE_XML) //retornar
				.body(book)
				.when()
				.put()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		BookVO persistedBook = xmlMapper.readValue(content, BookVO.class);
		book = persistedBook;
		
		bookAssertPersisted.setMockBook(book);
		
		bookAssertPersisted.assertUpdate(persistedBook);		
	}
	
	@Test
	@Order(4) //Simulando POSTMAN
	public void testDelete() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_XML) //receber
				.pathParams("id", book.getId()) //person está persistido no test 1
					.when()
				.delete("{id}")
					.then()
				.statusCode(204);
	}
	
	@Test
	@Order(5) //Simulando POSTMAN
	public void testFindAllWithPageableModel() throws JsonMappingException, JsonProcessingException {
				
		//a specification foi realizada no 0 test	
		
		var content =
			given() //REST Assured
			.spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_XML) //receber 
			.accept(TestConfig.CONTENT_TYPE_XML) //retornar
			.queryParams("page", 2, "size", 4, "direction", "asc")	
				.when()
				.get()
			.then()
				.statusCode(200)
			.extract()
				.body().asString();
		
		//List<BookVO> persistedBooks = xmlMapper.readValue(content, new TypeReference<List<BookVO>>(){});
		
		PagedModelBookVO pagedModel = xmlMapper.readValue(content, PagedModelBookVO.class);
		
		List<BookVO> persistedBooks = pagedModel.getContent();
		
		bookAssertPersisted.assertList(persistedBooks);		
	}
	
	@Test
	@Order(6) //Simulando POSTMAN
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
				
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfig.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		given() //REST Assured
			.spec(specificationWithoutToken)
			.contentType(TestConfig.CONTENT_TYPE_XML) //receber			
				.when()
				.get()
			.then()
				.statusCode(403);
	}
}
