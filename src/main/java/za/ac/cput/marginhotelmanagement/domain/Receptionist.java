package za.ac.cput.marginhotelmanagement.domain;
/* Receptionist.java
   Receptionist entity, extends Staff
   Author: Lithabile Lalela (221340963)
   Date: 21 June 2026 */

public class Receptionist extends Staff {
    private String deskNumber;

    private Receptionist() {
    }

    public Receptionist(Builder builder) {
        super(builder.staffId, builder.name, builder.contactDetails);
        this.deskNumber = builder.deskNumber;
    }

    public String getDeskNumber() {
        return deskNumber;
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "staffId=" + getStaffId() +
                ", name=" + getName() +
                ", contactDetails=" + getContactDetails() +
                ", deskNumber='" + deskNumber + '\'' +
                '}';
    }

    public static class Builder {
        private Long staffId;
        private Name name;
        private ContactDetails contactDetails;
        private String deskNumber;

        public Builder setStaffId(Long staffId) {
            this.staffId = staffId;
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

        public Builder setDeskNumber(String deskNumber) {
            this.deskNumber = deskNumber;
            return this;
        }

        public Receptionist build() {
            return new Receptionist(this);
        }
    }
}