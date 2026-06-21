package za.ac.cput.marginhotelmanagement.domain;
/* Invoice.java
   Invoice POJO class
   Author: MS MALAPILE (222904267)
   Date: 20 June 2026 */

import za.ac.cput.marginhotelmanagement.enums.InvoiceStatus;

import java.time.LocalDate;

public class Invoice {

    private Long invoiceId;
    private double totalAmount;
    private LocalDate issueDate;
    private String bookingId;
    private int guestId;
    private InvoiceStatus invoiceStatus;

    protected Invoice(){

    }

    public Invoice (Builder builder){
        this.invoiceId = builder.invoiceId;
        this.totalAmount = builder.totalAmount;
        this.issueDate= builder.issueDate;
        this.bookingId = builder.bookingId;
        this.guestId = builder.guestId;
        this.invoiceStatus = builder.invoiceStatus;

    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getBookingId(){
        return bookingId;
    }

    public int getGuestId(){
        return guestId;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", totalAmount=" + totalAmount +
                ", issueDate=" + issueDate +
                ", bookingId='" + bookingId + '\'' +
                ", guestId=" + guestId +
                ", invoiceStatus=" + invoiceStatus +
                '}';
    }

    public static class Builder{
        private Long invoiceId;
        private double totalAmount;
        private LocalDate issueDate;
        private String bookingId;
        private int guestId;
        private InvoiceStatus invoiceStatus;

        public Builder setInvoiceId(Long invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Builder setIssueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder setBookingId(String book){
            this.bookingId = bookingId;
            return this;
        }

        public Builder setGuestId(int guestId){
            this.guestId = guestId;
            return this;
        }

        public Builder setInvoiceStatus(InvoiceStatus invoiceStatus) {
            this.invoiceStatus = invoiceStatus;
            return this;
        }

        public Builder copy (Invoice invoice){
            this.invoiceId = invoice.invoiceId;
            this.totalAmount = invoice.totalAmount;
            this.issueDate = invoice.issueDate;
            this.bookingId = invoice.bookingId;
            this.guestId = invoice.guestId;
            this.invoiceStatus = invoice.invoiceStatus;
            return this;
        }

        public Invoice build(){
            return new Invoice(this);
        }
    }
}
