package za.ac.cput.marginhotelmanagement.domain;
/* Payment.java
   Payment POJO class
   Author: DM Madondo (230949703)
   Date: 20 June 2026 */

import za.ac.cput.marginhotelmanagement.enums.PaymentStatus;

import java.time.LocalDateTime;

public class Payment {
    private Long paymentId;
    private double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;

    private Payment() {
    }

    public Payment(Builder builder) {
        this.paymentId = builder.paymentId;
        this.amount = builder.amount;
        this.paymentStatus = builder.paymentStatus;
        this.paymentDate = builder.paymentDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", paymentStatus=" + paymentStatus +
                ", paymentDate=" + paymentDate +
                '}';
    }

    public static class Builder{
        private Long paymentId;
        private double amount;
        private PaymentStatus paymentStatus;
        private LocalDateTime paymentDate;

        public Builder setPaymentId(Long paymentId){
            this.paymentId = paymentId;
            return this;
        }
        public Builder setAmount(double amount){
            this.amount = amount;
            return this;
        }
        public Builder setPaymentStatus(PaymentStatus paymentStatus){
            this.paymentStatus = paymentStatus;
            return this;
        }
        public Builder setPaymentDate(LocalDateTime paymentDate){
            this.paymentDate = paymentDate;
            return this;
        }

        public Builder copy(Payment payment){
            this.paymentId = payment.paymentId;
            this.amount = payment.amount;
            this.paymentStatus = payment.paymentStatus;
            this.paymentDate = payment.paymentDate;
            return this;
        }

        public Payment build(){
            return new Payment(this);
        }
    }
}
