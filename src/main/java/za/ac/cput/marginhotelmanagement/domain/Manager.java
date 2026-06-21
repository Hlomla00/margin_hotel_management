package za.ac.cput.marginhotelmanagement.domain;
/* Manager.java
   Manager entity, extends Staff
   Author: Lithabile Lalela (221340963)
   Date: 21 June 2026 */

public class Manager extends Staff {
    private String officeNumber;

    private Manager() {
    }

    public Manager(Builder builder) {
        super(builder.staffId, builder.name, builder.contactDetails);
        this.officeNumber = builder.officeNumber;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "staffId=" + getStaffId() +
                ", name=" + getName() +
                ", contactDetails=" + getContactDetails() +
                ", officeNumber='" + officeNumber + '\'' +
                '}';
    }

    public static class Builder {
        private Long staffId;
        private Name name;
        private ContactDetails contactDetails;
        private String officeNumber;

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

        public Builder setOfficeNumber(String officeNumber) {
            this.officeNumber = officeNumber;
            return this;
        }

        public Manager build() {
            return new Manager(this);
        }
    }
}