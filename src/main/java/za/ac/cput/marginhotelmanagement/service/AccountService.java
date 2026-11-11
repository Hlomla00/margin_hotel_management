package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Matuma Malapile (22904267)
   Date: 11 November 2026
   */

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.marginhotelmanagement.domain.AppUser;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.dtos.auth.AuthUserResponse;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.UpdateContactRequest;
import za.ac.cput.marginhotelmanagement.repository.AppUserRepository;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

/*
   Lets a logged-in guest change their own email and mobile number.
   The name is never touched here: the request has no name fields and
   the existing Name is copied across unchanged.
   */
@Service
public class AccountService {

    private final AppUserRepository appUserRepository;
    private final GuestRepository guestRepository;
    private final JwtService jwtService;

    public AccountService(AppUserRepository appUserRepository,
                          GuestRepository guestRepository,
                          JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.guestRepository = guestRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthUserResponse updateContact(String currentEmail, UpdateContactRequest request) {
        String newEmail = request.getEmail() == null ? null : request.getEmail().trim();
        String newMobile = request.getMobile() == null ? null : request.getMobile().trim();

        if (Helper.isInvalidEmail(newEmail)) {
            throw new IllegalArgumentException("A valid email is required");
        }
        if (Helper.isInvalidMobile(newMobile)) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits");
        }

        AppUser appUser = appUserRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Guest guest = appUser.getGuest();
        if (guest == null) {
            throw new IllegalStateException("This account has no guest profile to update");
        }

        boolean emailChanged = !newEmail.equalsIgnoreCase(currentEmail);
        if (emailChanged) {
            if (appUserRepository.findByEmail(newEmail).isPresent()) {
                throw new IllegalStateException("That email is already in use");
            }
            boolean usedByOtherGuest = guestRepository.findByContactDetails_Email(newEmail).stream()
                    .anyMatch(g -> !g.getGuestId().equals(guest.getGuestId()));
            if (usedByOtherGuest) {
                throw new IllegalStateException("That email is already in use");
            }
        }

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(newEmail)
                .setMobile(newMobile)
                .build();

        // copy() keeps the existing Name, so the name cannot change here
        Guest updatedGuest = guestRepository.save(new Guest.Builder()
                .copy(guest)
                .setContactDetails(contactDetails)
                .build());

        // copy() keeps the password hash and role
        AppUser updatedUser = appUserRepository.save(new AppUser.Builder()
                .copy(appUser)
                .setEmail(newEmail)
                .setGuest(updatedGuest)
                .build());

        String token = jwtService.generateToken(updatedUser.getEmail());
        return new AuthUserResponse(token, updatedUser.getEmail(), updatedUser.getRole());
    }
}