package za.ac.cput.marginhotelmanagement.mappers;
/*
   GuestMapper.java
   Converts between the Guest entity and its DTOs. Plain static methods,
   same style as StaffMapper, since Name/ContactDetails use nested Builders
   that MapStruct doesn't map cleanly by default.
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
   Updated: 04 November 2026 — toDto no longer throws a NullPointerException
   when a guest has no name or contact details; returns null for a null guest.
   */

import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.GuestDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.UpdateGuestRequest;

public class GuestMapper {

    public static Guest toEntity(CreateGuestRequest request) {
        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
                .build();

        return new Guest.Builder()
                .setName(name)
                .setContactDetails(contactDetails)
                .build();
    }

    public static Guest toEntity(UpdateGuestRequest request) {
        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
                .build();

        return new Guest.Builder()
                .setGuestId(request.getGuestId())
                .setName(name)
                .setContactDetails(contactDetails)
                .build();
    }

    // Older rows may have no embedded name or contact details (JPA loads an
    // all-null embeddable as null), so those fields come back as null instead
    // of throwing a NullPointerException.
    public static GuestDto toDto(Guest guest) {
        if (guest == null) {
            return null;
        }
        Name name = guest.getName();
        ContactDetails contactDetails = guest.getContactDetails();
        return new GuestDto(
                guest.getGuestId(),
                name != null ? name.getFirstName() : null,
                name != null ? name.getMiddleName() : null,
                name != null ? name.getLastName() : null,
                contactDetails != null ? contactDetails.getEmail() : null,
                contactDetails != null ? contactDetails.getMobile() : null
        );
    }
}
