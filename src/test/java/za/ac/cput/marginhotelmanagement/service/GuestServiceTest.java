package za.ac.cput.marginhotelmanagement.service;
/*
    GuestServiceTest.java
    Test for GuestService
    Author: Hlomla Magopeni (218070349)
    Date: 16 July 2026
    Updated: 04 November 2026 — added negative tests (invalid email, invalid
    mobile, missing name, missing guest on read/update/delete) and a test for
    findByEmail when two guests share the same email.
    Updated: 04 November 2026 — tests for the guest rules: a new email every
    run, a duplicate email (any capitals or spaces) is refused, emails are
    stored lowercase, a guest with bookings or an online account can't be
    deleted, and an online guest's email can't be changed here. Everything
    the tests create is removed afterwards.
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.AppUser;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.enums.UserRole;
import za.ac.cput.marginhotelmanagement.factory.ContactDetailsFactory;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.factory.NameFactory;
import za.ac.cput.marginhotelmanagement.repository.AppUserRepository;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GuestServiceTest {

    @Autowired
    private GuestService guestService;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    private Guest mockGuest;
    private Name mockName;
    private ContactDetails mockContactDetails;

    // A different email every run, so guests left by earlier runs can't clash.
    private final String run = String.valueOf(System.currentTimeMillis());
    private final String mockEmail = "thandeka.zulu." + run + "@marginhotel.com";

    // Everything the tests create, removed in tearDown().
    private final List<Guest> createdGuests = new ArrayList<>();
    private final List<Booking> createdBookings = new ArrayList<>();
    private final List<AppUser> createdAppUsers = new ArrayList<>();

    @BeforeAll
    void setUp() {
        mockName = NameFactory.createName("Thandeka", "N", "Zulu");
        assertNotNull(mockName, "Mock name creation failed");

        mockContactDetails = ContactDetailsFactory.createContactDetails(
                mockEmail, "0821234567");
        assertNotNull(mockContactDetails, "Mock contact details creation failed");

        // No id passed in — guestId is @GeneratedValue(IDENTITY), so it must be null before persisting
        mockGuest = GuestFactory.createGuest(mockName, mockContactDetails);
        assertNotNull(mockGuest, "Mock guest creation failed");
    }

    @Test
    @Order(1)
    void create() {
        Guest created = guestService.create(mockGuest);
        assertNotNull(created);
        assertNotNull(created.getGuestId());
        mockGuest = created;
        createdGuests.add(created);
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Guest read = guestService.read(mockGuest.getGuestId());
        assertNotNull(read);
        assertEquals(mockGuest.getGuestId(), read.getGuestId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Name updatedName = new Name.Builder()
                .copy(mockGuest.getName())
                .setFirstName("Thandi")
                .build();

        Guest updated = new Guest.Builder()
                .copy(mockGuest)
                .setName(updatedName)
                .build();

        Guest result = guestService.update(updated);
        assertNotNull(result);
        assertEquals("Thandi", result.getName().getFirstName());
        mockGuest = result;
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    @Disabled
    void delete() {
        boolean deleted = guestService.delete(mockGuest);
        assertTrue(deleted);
        System.out.println("Deleted guest: " + mockGuest.getGuestId());
    }

    @Test
    @Order(5)
    void findAll() {
        List<Guest> guests = guestService.findAll();
        assertNotNull(guests);
        assertFalse(guests.isEmpty());
        System.out.println("All guests: " + guests);
    }

    @Test
    @Order(6)
    void findByFirstName() {
        List<Guest> guests = guestService.findByFirstName(mockGuest.getName().getFirstName());
        assertNotNull(guests);
        assertFalse(guests.isEmpty());
        System.out.println("Guests by first name: " + guests);
    }

    @Test
    @Order(7)
    void findByLastName() {
        List<Guest> guests = guestService.findByLastName(mockGuest.getName().getLastName());
        assertNotNull(guests);
        assertFalse(guests.isEmpty());
        System.out.println("Guests by last name: " + guests);
    }

    @Test
    @Order(8)
    void findByEmail() {
        Guest guest = guestService.findByEmail(mockGuest.getContactDetails().getEmail());
        assertNotNull(guest);
        assertEquals(mockGuest.getGuestId(), guest.getGuestId());
        System.out.println("Guest by email: " + guest);
    }

    /* ==== Negative tests ==== */

    private static final Long MISSING_ID = Long.MAX_VALUE;

    private Guest guestWith(Name name, String email, String mobile) {
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(email)
                .setMobile(mobile)
                .build();
        return new Guest.Builder()
                .setName(name)
                .setContactDetails(contactDetails)
                .build();
    }

    @Test
    @Order(9)
    void createWithInvalidEmailThrows() {
        Guest guest = guestWith(mockName, "not-an-email", "0821234567");
        assertThrows(IllegalArgumentException.class, () -> guestService.create(guest));
    }

    @Test
    @Order(10)
    void createWithInvalidMobileThrows() {
        Guest guest = guestWith(mockName, "valid.guest@marginhotel.com", "082123456");
        assertThrows(IllegalArgumentException.class, () -> guestService.create(guest));
    }

    @Test
    @Order(11)
    void createWithMissingNameThrows() {
        Guest guest = guestWith(null, "valid.guest@marginhotel.com", "0821234567");
        assertThrows(IllegalArgumentException.class, () -> guestService.create(guest));
    }

    @Test
    @Order(12)
    void createWithMissingLastNameThrows() {
        Name noLastName = new Name.Builder().setFirstName("Thandeka").build();
        Guest guest = guestWith(noLastName, "valid.guest@marginhotel.com", "0821234567");
        assertThrows(IllegalArgumentException.class, () -> guestService.create(guest));
    }

    @Test
    @Order(13)
    void readMissingGuestReturnsNull() {
        assertNull(guestService.read(MISSING_ID));
    }

    @Test
    @Order(14)
    void updateMissingGuestReturnsNull() {
        Guest missing = new Guest.Builder().copy(mockGuest).setGuestId(MISSING_ID).build();
        assertNull(guestService.update(missing));
    }

    @Test
    @Order(15)
    void updateWithInvalidEmailThrows() {
        ContactDetails badEmail = new ContactDetails.Builder()
                .setEmail("not-an-email")
                .setMobile(mockGuest.getContactDetails().getMobile())
                .build();
        Guest invalid = new Guest.Builder().copy(mockGuest).setContactDetails(badEmail).build();
        assertThrows(IllegalArgumentException.class, () -> guestService.update(invalid));
    }

    @Test
    @Order(16)
    void deleteMissingGuestReturnsFalse() {
        assertFalse(guestService.deleteGuest(MISSING_ID));
    }

    @Test
    @Order(17)
    void createWithDuplicateEmailIsRefused() {
        // Same email typed with capitals and spaces is still the same guest.
        String sameEmailTypedDifferently = "  " + mockEmail.toUpperCase() + " ";
        Guest duplicate = GuestFactory.createGuest(
                NameFactory.createName("Sipho", null, "Zulu"),
                ContactDetailsFactory.createContactDetails(sameEmailTypedDifferently, "0831234567"));
        assertThrows(GuestService.GuestConflictException.class, () -> guestService.create(duplicate));
    }

    /* ==== Guest rules: email handling ==== */

    private Guest createGuest(String firstName, String email) {
        Guest created = guestService.create(GuestFactory.createGuest(
                NameFactory.createName(firstName, null, "Zulu"),
                ContactDetailsFactory.createContactDetails(email, "0831234567")));
        createdGuests.add(created);
        return created;
    }

    @Test
    @Order(18)
    void emailIsStoredTrimmedAndLowercase() {
        Guest created = createGuest("Ayanda", "  Ayanda.Khumalo." + run + "@MarginHotel.COM ");
        assertEquals("ayanda.khumalo." + run + "@marginhotel.com", created.getContactDetails().getEmail());
    }

    @Test
    @Order(19)
    void findByEmailIgnoresCapitals() {
        Guest found = guestService.findByEmail(mockEmail.toUpperCase());
        assertNotNull(found);
        assertEquals(mockGuest.getGuestId(), found.getGuestId());
    }

    @Test
    @Order(20)
    void changingEmailToAnotherGuestsEmailIsRefused() {
        Guest other = createGuest("Lerato", "lerato.zulu." + run + "@marginhotel.com");
        ContactDetails takenEmail = new ContactDetails.Builder()
                .copy(other.getContactDetails())
                .setEmail(mockEmail)
                .build();
        Guest clash = new Guest.Builder().copy(other).setContactDetails(takenEmail).build();
        assertThrows(GuestService.GuestConflictException.class, () -> guestService.update(clash));
    }

    @Test
    @Order(21)
    void changingEmailOfGuestWithOnlineAccountIsRefused() {
        String onlineEmail = "online.guest." + run + "@marginhotel.com";
        Guest onlineGuest = createGuest("Naledi", onlineEmail);
        createdAppUsers.add(appUserRepository.save(new AppUser.Builder()
                .setEmail(onlineEmail)
                .setPassword("not-a-real-hash")
                .setRole(UserRole.USER)
                .setGuest(onlineGuest)
                .build()));

        ContactDetails newEmail = new ContactDetails.Builder()
                .copy(onlineGuest.getContactDetails())
                .setEmail("naledi.new." + run + "@marginhotel.com")
                .build();
        Guest changed = new Guest.Builder().copy(onlineGuest).setContactDetails(newEmail).build();
        assertThrows(GuestService.GuestConflictException.class, () -> guestService.update(changed));

        // Changing only the mobile number is still allowed.
        ContactDetails newMobile = new ContactDetails.Builder()
                .copy(onlineGuest.getContactDetails())
                .setMobile("0849998888")
                .build();
        Guest mobileOnly = new Guest.Builder().copy(onlineGuest).setContactDetails(newMobile).build();
        Guest updated = guestService.update(mobileOnly);
        assertEquals("0849998888", updated.getContactDetails().getMobile());

        // ...and the account also stops the guest record from being deleted.
        assertThrows(GuestService.GuestConflictException.class, () -> guestService.deleteGuest(onlineGuest.getGuestId()));
    }

    /* ==== Guest rules: deleting ==== */

    @Test
    @Order(22)
    void deleteGuestWithBookingsIsRefused() {
        Guest bookedGuest = createGuest("Bongani", "bongani.zulu." + run + "@marginhotel.com");
        createdBookings.add(bookingRepository.save(new Booking.Builder()
                .setBookingDate(LocalDate.now())
                .setGuest(bookedGuest)
                .build()));

        assertThrows(GuestService.GuestConflictException.class, () -> guestService.deleteGuest(bookedGuest.getGuestId()));
        assertNotNull(guestService.read(bookedGuest.getGuestId()), "The guest must still exist");
    }

    @Test
    @Order(23)
    void deleteGuestWithoutHistorySucceeds() {
        Guest newGuest = createGuest("Zodwa", "zodwa.zulu." + run + "@marginhotel.com");
        assertTrue(guestService.deleteGuest(newGuest.getGuestId()));
        assertNull(guestService.read(newGuest.getGuestId()));
    }

    @AfterAll
    void tearDown() {
        // Remove what this class created, children first, so other test classes start clean.
        createdBookings.forEach(booking -> bookingRepository.deleteById(booking.getBookingId()));
        appUserRepository.deleteAll(createdAppUsers);
        createdGuests.stream()
                .filter(guest -> guest != null && guestRepository.existsById(guest.getGuestId()))
                .forEach(guestRepository::delete);
    }
}