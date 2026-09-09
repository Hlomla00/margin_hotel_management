package za.ac.cput.marginhotelmanagement.dtos;
/*
   Author: Hlomla Magopeni (218070349)
   Date: 09 September 2026
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
