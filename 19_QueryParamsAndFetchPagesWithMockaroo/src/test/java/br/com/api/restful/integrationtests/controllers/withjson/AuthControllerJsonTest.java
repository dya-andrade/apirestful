package br.com.api.restful.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.api.restful.configs.TestConfig;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.api.restful.integrationtests.vo.AccountCredentialsVO;
import br.com.api.restful.integrationtests.vo.TokenVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //port 8888
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest { //TestContainers
	
	private static TokenVO tokenVO;
	
	@Test
	@Order(1) //Test Auth, simulando POSTMAN
	public void testSignin() {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		tokenVO = 
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
						.body().as(TokenVO.class); //não possui atributos desconhecidos
						
		assertNotNull(tokenVO.getAccessToken());
		assertNotNull(tokenVO.getRefreshToken());
		
	}
	
	@Test
	@Order(2) //Test Auth, simulando POSTMAN
	public void testRefresh() {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var newTokenVO = 
				given() //REST Assured
				.basePath("/auth/refresh")
					.port(TestConfig.SERVER_PORT)
					.contentType(TestConfig.CONTENT_TYPE_JSON)
				.pathParam("username", user.getUsername())
				.header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
					.when()
				.put("{username}")
					.then()
						.statusCode(200)
					.extract()
						.body().as(TokenVO.class); //não possui atributos desconhecidos
						
		assertNotNull(newTokenVO.getAccessToken());
		assertNotNull(newTokenVO.getRefreshToken());
		
	}

}
