package br.com.api.restful.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.api.restful.configs.TestConfig;
import br.com.api.restful.integrationtests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
public class SwaggerIntegrationTest extends AbstractIntegrationTest { //TestContainers

	@Test //testa integração com Swagger
	public void shouldDisplaySwaggerUiPage() {
		var content =
			given() //REST Assured
				.basePath("/swagger-ui/index.html")
				.port(TestConfig.SERVER_PORT)
				.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body().asString();
		
		assertTrue(content.contains("Swagger UI")); //JUnit
	}

}
