package za.ac.cput.marginhotelmanagement.domain.dtos.guest;
/*
   UpdateGuestRequest.java
   Request body for updating an existing Guest
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
   Updated: 04 November 2026 — header brought in line with the other Guest files.
   */

import lombok.Data;

@Data
public class UpdateGuestRequest {
    private Long guestId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
}
