package za.ac.cput.marginhotelmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;

/*
   Full shape returned to the client on reads. Flattens the linked Booking
   down to bookingId instead of nesting the whole Booking entity.
   Author: Matuma Malapile (222904267)
   Date: 07 September 2026
   */
@AllArgsConstructor
@Getter
public class InvoiceDto {
    private Long invoiceId;
    private String reference;
    private double totalAmount;
    private InvoiceStatus status;
    private LocalDate issueDate;
    private Long bookingId;
}