package com.djem.endpoint;

import com.djem.dto.ShipmentDto;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mahdi Sharifi
 * @see com.djem.endpoint.ShipmentEndpoint
 */

@QuarkusTest
class ShipmentEndpointTest {

    @Test
    void countShipmentsOnMaria() {
        given()
                .header(ACCEPT, TEXT_PLAIN).
        when()
                .get("/shipments/count-maria").
        then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void countShipmentOnPostgres() {
        given()
                .header(ACCEPT, TEXT_PLAIN).
        when()
                .get("/shipments/count-postgres").
        then()
                .statusCode(OK.getStatusCode());
    }

    @Test
    void getSingleShipment() {
    given()
            .header(ACCEPT, APPLICATION_JSON)
            .pathParam("id", 1).
    when()
            .get("/shipments/{id}").
    then()
            .statusCode(OK.getStatusCode())
            .body("name", Is.is("mahdi"));
    }

    @Test
    void createShipment() {
    ShipmentDto dto= new ShipmentDto("Shipment#144");
    given()
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .body(dto)
    .when()
            .post("/shipments")
    .then()
            .statusCode(CREATED.getStatusCode());
    }
}