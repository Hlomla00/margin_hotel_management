package za.ac.cput.marginhotelmanagement.domain.dtos.guest;
/*
   Author: Matuma Malapile (22904267)
   Date: 11 November 2026
   */
import lombok.Data;

@Data
public class UpdateContactRequest {
    private String email;
    private String mobile;
}