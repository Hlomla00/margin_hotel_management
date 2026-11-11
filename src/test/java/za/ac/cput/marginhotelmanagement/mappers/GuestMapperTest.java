package za.ac.cput.marginhotelmanagement.mappers;
/*
   GuestMapperTest.java
   Test for GuestMapper (no database needed)
   Author: Hlomla Magopeni (218070349)
   Date: 04 November 2026
   */

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.GuestDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.UpdateGuestRequest;
import za.ac.cput.marginhotelmanagement.factory.ContactDetailsFactory;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.factory.NameFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GuestMapperTest {

    @Test
    @Order(1)
    void toEntityFromCreateRequest() {
        CreateGuestRequest request = new CreateGuestRequest();
        request.setFirstName("Thandeka");
        request.setMiddleName("N");
        request.setLastName("Zulu");
        request.setEmail("thandeka.zulu@marginhotel.com");
        request.setMobile("0821234567");

        Guest guest = GuestMapper.toEntity(request);
        assertNull(guest.getGuestId());
        assertEquals("Thandeka", guest.getName().getFirstName());
        assertEquals("N", guest.getName().getMiddleName());
        assertEquals("Zulu", guest.getName().getLastName());
        assertEquals("thandeka.zulu@marginhotel.com", guest.getContactDetails().getEmail());
        assertEquals("0821234567", guest.getContactDetails().getMobile());
    }

    @Test
    @Order(2)
    void toEntityFromUpdateRequestKeepsId() {
        UpdateGuestRequest request = new UpdateGuestRequest();
        request.setGuestId(7L);
        request.setFirstName("Thandi");
        request.setLastName("Zulu");
        request.setEmail("thandi.zulu@marginhotel.com");
        request.setMobile("0821234567");

        Guest guest = GuestMapper.toEntity(request);
        assertEquals(7L, guest.getGuestId());
        assertEquals("Thandi", guest.getName().getFirstName());
    }

    @Test
    @Order(3)
    void toDtoFlattensNameAndContactDetails() {
        Guest guest = GuestFactory.createGuest(5L,
                NameFactory.createName("Thandeka", "N", "Zulu"),
                ContactDetailsFactory.createContactDetails("thandeka.zulu@marginhotel.com", "0821234567"));
        assertNotNull(guest);

        GuestDto dto = GuestMapper.toDto(guest);
        assertEquals(5L, dto.getGuestId());
        assertEquals("Thandeka", dto.getFirstName());
        assertEquals("N", dto.getMiddleName());
        assertEquals("Zulu", dto.getLastName());
        assertEquals("thandeka.zulu@marginhotel.com", dto.getEmail());
        assertEquals("0821234567", dto.getMobile());
    }

    @Test
    @Order(4)
    void toDtoWithMissingNameAndContactDetailsDoesNotThrow() {
        // An older row can load with no embedded name or contact details.
        Guest guest = new Guest.Builder().setGuestId(9L).build();

        GuestDto dto = assertDoesNotThrow(() -> GuestMapper.toDto(guest));
        assertEquals(9L, dto.getGuestId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getEmail());
        assertNull(dto.getMobile());
    }

    @Test
    @Order(5)
    void toDtoWithNullGuestReturnsNull() {
        assertNull(GuestMapper.toDto(null));
    }
}
