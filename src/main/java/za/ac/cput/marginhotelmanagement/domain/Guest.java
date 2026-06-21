package za.ac.cput.marginhotelmanagement.domain;
/*
   Guest.java
   Guest POJO class
   Author: Hlomla Magopeni (218070349)
   Date: 21 June 2026 */


public class Guest {
    private Long guestId;
    private Name name;
    private ContactDetails contactDetails;

    private Guest() {}

    public Guest(Builder builder) {
        this.guestId = builder.guestId;
        this.name = builder.name;
        this.contactDetails = builder.contactDetails;
    }

    public Long getGuestId() {
        return guestId;
    }

    public Name getName() {
        return name;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", name=" + name +
                ", contactDetails=" + contactDetails +
                '}';
    }

    public static class Builder {
        private Long guestId;
        private Name name;
        private ContactDetails contactDetails;

        public Builder setGuestId(Long guestId) {
            this.guestId = guestId;
            return this;
        }

        public Builder setName(Name name) {
            this.name = name;
            return this;
        }

        public Builder setContactDetails(ContactDetails contactDetails) {
            this.contactDetails = contactDetails;
            return this;
        }

        public Builder copy(Guest guest) {
            this.guestId = guest.guestId;
            this.name = guest.name;
            this.contactDetails = guest.contactDetails;
            return this;
        }

        public Guest build() {
            return new Guest(this);
        }
    }
}