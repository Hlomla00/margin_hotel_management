package za.ac.cput.marginhotelmanagement.domain.dtos.guest;
/*
   CreateGuestRequest.java
   Request body for creating a Guest
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
   Updated: 04 November 2026 — header brought in line with the other Guest files.
   */

import lombok.Data;

@Data
public class CreateGuestRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
}
