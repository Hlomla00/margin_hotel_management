package za.ac.cput.marginhotelmanagement.domain;
/*
   Author: Katlego Malaka(230443370)
   Date: 20 June 2026 */


import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDate;

public class Booking {
    private Long bookingId;
    private LocalDate bookingDate;
    private StayPeriod stayPeriod;
    private BookingChannel bookingChannel;
    private Guest guest;
    private Room room;

    private Booking() {
    }
    public Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.bookingDate = builder.bookingDate;
        this.stayPeriod = builder.stayPeriod;
        this.bookingChannel = builder.bookingChannel;
        this.guest = builder.guest;
        this.room = builder.room;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Room getRoom() {
        return room;
    }

    public Guest getGuest() {
        return guest;
    }



    public BookingChannel getBookingChannel() {
        return bookingChannel;
    }

    public StayPeriod getStayPeriod() {
        return stayPeriod;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingDate=" + bookingDate +
                ", stayPeriod=" + stayPeriod +
                ", bookingChannel=" + bookingChannel +
                ", guest=" + guest +
                ", room=" + room +
                '}';
    }

    private static class Builder{
        private Long bookingId;
        private LocalDate bookingDate;
        private StayPeriod stayPeriod;
        private BookingChannel bookingChannel;
        private Guest guest;
        private Room room;



        public Builder setBookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }
        public Builder setBookingDate(LocalDate bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public Builder setStayPeriod(StayPeriod stayPeriod) {
            this.stayPeriod = stayPeriod;
            return this;
        }

        public Builder setBookingChannel(BookingChannel bookingChannel) {
            this.bookingChannel = bookingChannel;
            return this;
        }

        public Builder setGuest(Guest guest) {
            this.guest = guest;
            return this;
        }
        public Builder copy(Booking booking){
            this.bookingId = booking.bookingId;
            this.bookingDate = booking.bookingDate;
            this.stayPeriod = booking.stayPeriod;
            this.bookingChannel = booking.bookingChannel;
            this.guest = booking.guest;
            this.room = booking.room;
            return this;
        }
        public Booking build(){
            return new Booking(this);
        }
    }
}
