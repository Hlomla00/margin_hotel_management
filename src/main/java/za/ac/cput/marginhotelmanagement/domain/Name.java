package za.ac.cput.marginhotelmanagement.domain;
/*
   Name.java
   Name POJO class
   Author: Hlomla Magopeni (218070349)
   Date: 21 June 2026 */


public final class Name implements ValueObject {
    private String firstName;
    private String middleName;
    private String lastName;

    private Name() {}

    public Name(Builder builder) {
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Name{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public static class Builder {
        private String firstName;
        private String middleName;
        private String lastName;
    }
    public Builder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public Builder setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }
    public Builder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    public Builder copy(Name name) {
        this.firstName = name.firstName;
        this.middleName = name.middleName;
        this.lastName = name.lastName;
        return this;
    }

    public Name build() {
        return new Name(this);
    }

}