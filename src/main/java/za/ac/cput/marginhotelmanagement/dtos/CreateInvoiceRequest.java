package za.ac.cput.marginhotelmanagement.dtos;

import lombok.Data;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;

/*
   Author: Matuma Malapile (222904267)
   Date: 07 September 2026
   */
@Data
public class CreateInvoiceRequest {
    private String reference;
    private double totalAmount;
    private InvoiceStatus status;
    private LocalDate issueDate;
    private Long bookingId; // reference an existing booking
}