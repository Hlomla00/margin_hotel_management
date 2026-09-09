package za.ac.cput.marginhotelmanagement.controller;
/*
   GuestControllerTest.java
   Author: Hlomla Magopeni (218070349)
   Date: 21 August 2026
   Updated: 09 September 2026 — rewritten against the DTO-based contract
   (CreateGuestRequest / GuestDto / UpdateGuestRequest) that
   GuestController actually exposes, and fixed to hit the app's real
   "/marginhotel" context path on a random port instead of a hardcoded
   port 8080.
   */

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.marginhotelmanagement.dtos.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.dtos.GuestDto;
import za.ac.cput.marginhotelmanagement.dtos.UpdateGuestRequest;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GuestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GuestRepository guestRepository;

    private Long guestId;

    private String BASE_URL() {
        return "http://localhost:" + port + "/marginhotel/guest";
    }

    @BeforeAll
    void setUp() {
        guestRepository.deleteAll();
    }

    @Test
    @Order(1)
    void create() {
        CreateGuestRequest request = new CreateGuestRequest();
        request.setFirstName("Hlomla");
        request.setMiddleName("M");
        request.setLastName("Magopeni");
        request.setEmail("hlomla.magopeni@example.com");
        request.setMobile("0821234567");

        String url = BASE_URL() + "/create";
        ResponseEntity<GuestDto> response = this.restTemplate.postForEntity(url, request, GuestDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        GuestDto created = response.getBody();
        assertNotNull(created, "Guest should be created");
        assertNotNull(created.getGuestId());
        assertEquals("Hlomla", created.getFirstName());
        assertEquals("hlomla.magopeni@example.com", created.getEmail());

        guestId = created.getGuestId();
        System.out.println("Created Guest: " + created);
    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL() + "/read/" + guestId;
        ResponseEntity<GuestDto> response = this.restTemplate.getForEntity(url, GuestDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GuestDto read = response.getBody();
        assertNotNull(read);
        assertEquals(guestId, read.getGuestId());
        System.out.println("Read Guest: " + read);
    }

    @Test
    @Order(3)
    void readMissingReturnsNotFound() {
        String url = BASE_URL() + "/read/999999999";
        ResponseEntity<GuestDto> response = this.restTemplate.getForEntity(url, GuestDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void update() {
        UpdateGuestRequest request = new UpdateGuestRequest();
        request.setGuestId(guestId);
        request.setFirstName("Hlomla");
        request.setMiddleName("M");
        request.setLastName("Magopeni");
        request.setEmail("hlomla.updated@example.com");
        request.setMobile("0839876543");

        String url = BASE_URL() + "/update";
        HttpEntity<UpdateGuestRequest> entity = new HttpEntity<>(request);
        ResponseEntity<GuestDto> response = this.restTemplate.exchange(
                url, HttpMethod.PUT, entity, GuestDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GuestDto updated = response.getBody();
        assertNotNull(updated);
        assertEquals("hlomla.updated@example.com", updated.getEmail());
        System.out.println("Updated Guest: " + updated);
    }

    @Test
    @Order(5)
    void updateMissingReturnsNotFound() {
        UpdateGuestRequest request = new UpdateGuestRequest();
        request.setGuestId(999999999L);
        request.setFirstName("Ghost");
        request.setLastName("Guest");
        request.setEmail("ghost@example.com");
        request.setMobile("0800000000");

        String url = BASE_URL() + "/update";
        HttpEntity<UpdateGuestRequest> entity = new HttpEntity<>(request);
        ResponseEntity<String> response = this.restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(6)
    void getAll() {
        String url = BASE_URL() + "/getall";
        ResponseEntity<GuestDto[]> response = this.restTemplate.getForEntity(url, GuestDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Get All Guests: " + response.getBody().length);
    }

    @Test
    @Order(7)
    void findByFirstName() {
        String url = BASE_URL() + "/findByFirstName/Hlomla";
        ResponseEntity<GuestDto[]> response = this.restTemplate.getForEntity(url, GuestDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Find Guests by First Name: " + response.getBody().length);
    }

    @Test
    @Order(8)
    void findByLastName() {
        String url = BASE_URL() + "/findByLastName/Magopeni";
        ResponseEntity<GuestDto[]> response = this.restTemplate.getForEntity(url, GuestDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        System.out.println("Find Guests by Last Name: " + response.getBody().length);
    }

    @Test
    @Order(9)
    void findByEmail() {
        String url = BASE_URL() + "/findByEmail/hlomla.updated@example.com";
        ResponseEntity<GuestDto> response = this.restTemplate.getForEntity(url, GuestDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        GuestDto found = response.getBody();
        assertNotNull(found);
        assertEquals(guestId, found.getGuestId());
        System.out.println("Find Guest by Email: " + found);
    }

    @Test
    @Order(10)
    void delete() {
        String url = BASE_URL() + "/delete/" + guestId;
        this.restTemplate.delete(url);

        ResponseEntity<GuestDto> response = this.restTemplate.getForEntity(
                BASE_URL() + "/read/" + guestId, GuestDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        System.out.println("Deleted Guest #" + guestId);
    }
}
