package za.ac.cput.marginhotelmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
   Flat shape returned to the client on reads. Flattens the embedded
   Name and ContactDetails value objects, same as ManagerDto/ReceptionistDto.
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
   */
@AllArgsConstructor
@Getter
public class GuestDto {
    private Long guestId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
}
