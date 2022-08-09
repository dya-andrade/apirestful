package br.com.api.restful.integrationtests.controllers.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.api.restful.configs.TestConfig;
import br.com.api.restful.integrationtests.controllers.withyaml.mapper.YMLMapper;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.AccountCredentialsVO;
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
public class AuthControllerYamlTest extends AbstractIntegrationTest {
	
	private static YMLMapper objectMapper;
	private static TokenVO tokenVO;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();
	}
	
	@Test
	@Order(1) //Test Auth, simulando POSTMAN
	public void testSignin() {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		//criação do log
		RequestSpecification specification = new RequestSpecBuilder()
							.addFilter(new RequestLoggingFilter(LogDetail.ALL))
							.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		
		tokenVO = //REST Assured não tem um mapper para YML, apenas para JSON E XML
				given() //REST Assured
				.spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
				.accept(TestConfig.CONTENT_TYPE_YML)
				.basePath("/auth/signin")
					.port(TestConfig.SERVER_PORT)
					.contentType(TestConfig.CONTENT_TYPE_YML) 
				.body(user, objectMapper) //parse
					.when()
				.post()
					.then()
						.statusCode(200)
					.extract()
						.body().as(TokenVO.class, objectMapper); //parse
						
		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());
		
	}
	
	@Test
	@Order(2) //Test Auth, simulando POSTMAN
	public void testRefresh() {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var newTokenVO = 
				given() //REST Assured
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT)))
				.accept(TestConfig.CONTENT_TYPE_YML)
				.basePath("/auth/refresh")
					.port(TestConfig.SERVER_PORT)
					.contentType(TestConfig.CONTENT_TYPE_YML) //REST Assured não tem um mapper para YML, apenas para JSON E XML
				.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
				.pathParam("username", user.getUsername())
					.when()
				.put("{username}")
					.then()
						.statusCode(200)
					.extract()
						.body().as(TokenVO.class, objectMapper); //parse
						
		assertNotNull(newTokenVO.getAccessToken());
		assertNotNull(newTokenVO.getRefreshToken());
		
	}
}
