package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between the Guest entity and its DTOs. Plain static methods,
   same style as StaffMapper, since Name/ContactDetails use nested Builders
   that MapStruct doesn't map cleanly by default.
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
   */

import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.dtos.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.dtos.GuestDto;
import za.ac.cput.marginhotelmanagement.dtos.UpdateGuestRequest;

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

    public static GuestDto toDto(Guest guest) {
        return new GuestDto(
                guest.getGuestId(),
                guest.getName().getFirstName(),
                guest.getName().getMiddleName(),
                guest.getName().getLastName(),
                guest.getContactDetails().getEmail(),
                guest.getContactDetails().getMobile()
        );
    }
}
