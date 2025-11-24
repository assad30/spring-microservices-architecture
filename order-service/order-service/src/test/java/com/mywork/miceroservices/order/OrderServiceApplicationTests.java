package com.mywork.miceroservices.order;

import com.mywork.miceroservices.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.33")
			.withDatabaseName("order_service")
			.withUsername("root")
			.withPassword("mysql")
			.withStartupTimeout(Duration.ofSeconds(60));



	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
                {
                     "skuCode": "iphone_15",
                     "price": 1000,
                     "quantity": 1
                }
                """;
		InventoryClientStub.stubInventoryCall("iphone_15",1);


		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();

		assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
	}

	@Test
	void shouldFailToSubmitOrderWhenInventoryNotAvailable() {
		String submitOrderJson = """
            {
                 "skuCode": "iphone_15",
                 "price": 1000,
                 "quantity": 1
            }
            """;

		// Stub inventory to return false
		InventoryClientStub.stubInventoryCallNegative("iphone_15", 100);

		// Call the order API
		var response = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(400) // assuming your service returns 400 when out of stock
				.extract()
				.body().asString();

		assertThat(response, Matchers.is("Order Failed: Product is out of stock"));
	}

}
