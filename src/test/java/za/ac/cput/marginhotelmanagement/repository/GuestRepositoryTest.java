package za.ac.cput.marginhotelmanagement.repository;
/*
   GuestRepositoryTest.java
   Test for GuestRepository
   Author: Hlomla Magopeni (218070349)
   Date: 04 November 2026
   */

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.factory.ContactDetailsFactory;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.factory.NameFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GuestRepositoryTest {

    @Autowired
    private GuestRepository guestRepository;

    private Guest guest;

    // A unique email per run, so rows left behind by earlier runs don't affect the finders.
    private final String email = "repo.test." + System.currentTimeMillis() + "@marginhotel.com";

    @BeforeAll
    void setUp() {
        Name name = NameFactory.createName("Ayanda", "L", "Mokoena");
        ContactDetails contactDetails = ContactDetailsFactory.createContactDetails(email, "0721234567");
        guest = GuestFactory.createGuest(name, contactDetails);
        assertNotNull(guest, "Guest creation failed");
    }

    @Test
    @Order(1)
    void createGuest() {
        guest = guestRepository.save(guest);
        assertNotNull(guest);
        assertNotNull(guest.getGuestId());
        System.out.println("Guest created successfully: " + guest);
    }

    @Test
    @Order(2)
    void readGuest() {
        Guest read = guestRepository.findById(guest.getGuestId()).orElse(null);
        assertNotNull(read);
        assertEquals("Ayanda", read.getName().getFirstName());
        assertEquals(email, read.getContactDetails().getEmail());
        System.out.println("Guest read successfully: " + read);
    }

    @Test
    @Order(3)
    void updateGuest() {
        Name updatedName = new Name.Builder().copy(guest.getName()).setFirstName("Ayanda-Lee").build();
        Guest updated = guestRepository.save(new Guest.Builder().copy(guest).setName(updatedName).build());
        assertNotNull(updated);
        assertEquals(guest.getGuestId(), updated.getGuestId());
        assertEquals("Ayanda-Lee", updated.getName().getFirstName());
        guest = updated;
        System.out.println("Guest updated successfully: " + updated);
    }

    @Test
    @Order(4)
    void findAll() {
        List<Guest> guests = guestRepository.findAll();
        assertNotNull(guests);
        assertTrue(guests.stream().anyMatch(g -> g.getGuestId().equals(guest.getGuestId())));
        System.out.println("All guests retrieved successfully: " + guests.size());
    }

    @Test
    @Order(5)
    void findByFirstName() {
        List<Guest> guests = guestRepository.findByName_FirstName("Ayanda-Lee");
        assertTrue(guests.stream().anyMatch(g -> g.getGuestId().equals(guest.getGuestId())));
        System.out.println("Guests by first name: " + guests);
    }

    @Test
    @Order(6)
    void findByLastName() {
        List<Guest> guests = guestRepository.findByName_LastName("Mokoena");
        assertTrue(guests.stream().anyMatch(g -> g.getGuestId().equals(guest.getGuestId())));
        System.out.println("Guests by last name: " + guests);
    }

    @Test
    @Order(7)
    void findByEmail() {
        List<Guest> guests = guestRepository.findByContactDetails_Email(email);
        assertEquals(1, guests.size());
        assertEquals(guest.getGuestId(), guests.get(0).getGuestId());
        System.out.println("Guests by email: " + guests);
    }

    @Test
    @Order(8)
    void findByUnknownEmailReturnsEmptyList() {
        List<Guest> guests = guestRepository.findByContactDetails_Email("nobody." + email);
        assertNotNull(guests);
        assertTrue(guests.isEmpty());
    }

    @Test
    @Order(9)
    void deleteGuest() {
        guestRepository.deleteById(guest.getGuestId());
        assertFalse(guestRepository.existsById(guest.getGuestId()));
        System.out.println("Guest deleted successfully: " + guest.getGuestId());
    }
}
