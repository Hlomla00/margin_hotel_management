package za.ac.cput.marginhotelmanagement.domain.dtos.guest;
/*
   GuestDto.java
   Flat shape returned to the client on reads. Flattens the embedded
   Name and ContactDetails value objects, same as ManagerDto/ReceptionistDto.
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
   Updated: 04 November 2026 — header moved above the imports, in line with
   the other Guest files.
   */

import lombok.AllArgsConstructor;
import lombok.Getter;

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
