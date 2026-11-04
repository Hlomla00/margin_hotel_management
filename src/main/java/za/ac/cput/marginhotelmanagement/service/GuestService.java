package za.ac.cput.marginhotelmanagement.service;
/*
    GuestService.java
    Service implementation for Guest entity
    Author: Hlomla Magopeni (218070349)
    Date: 16 July 2026
    Updated: 09 September 2026 — added the DTO-based create/read/update/delete
    methods (CreateGuestRequest / GuestDto / UpdateGuestRequest) that
    GuestController now calls, same pattern as PaymentService/BookingService.
    Updated: 04 November 2026 — guest existence is now checked once, in update(),
    before validation (a missing guest still returns 404 first); updateGuest()
    no longer repeats the existsById call.
    Updated: 04 November 2026 — guest rules, the way a hotel front desk works:
    emails are trimmed and stored in lowercase and searched ignoring capitals;
    a second guest with an email that is already used is refused (the caller
    gets the existing guest instead); a guest with bookings or an online
    account can't be deleted; the email of a guest with an online account
    can't be changed here.
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.GuestDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.UpdateGuestRequest;
import za.ac.cput.marginhotelmanagement.mappers.GuestMapper;
import za.ac.cput.marginhotelmanagement.repository.AppUserRepository;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.util.List;
import java.util.Locale;

@Service
public class GuestService implements IGuestService {

    private GuestRepository guestRepository;
    private BookingRepository bookingRepository;
    private AppUserRepository appUserRepository;

    @Autowired
    GuestService(GuestRepository guestRepository,
                 BookingRepository bookingRepository,
                 AppUserRepository appUserRepository) {
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
        this.appUserRepository = appUserRepository;
    }

    // Thrown when a request is valid but clashes with what is already stored
    // (duplicate email, guest still has bookings, email linked to a login).
    // GuestController turns it into 409 Conflict.
    public static class GuestConflictException extends IllegalStateException {
        public GuestConflictException(String message) {
            super(message);
        }
    }

    @Override
    public Guest create(Guest guest) {
        validate(guest);
        Guest normalised = normaliseEmail(guest);
        if (emailBelongsToAnotherGuest(normalised.getContactDetails().getEmail(), null)) {
            throw new GuestConflictException("A guest with this email already exists");
        }
        return guestRepository.save(normalised);
    }

    @Override
    public Guest read(Long id) {
        return guestRepository.findById(id).orElse(null);
    }

    @Override
    public Guest update(Guest guest) {
        if (guest.getGuestId() == null) {
            return null; // controller turns this into a 404
        }
        Guest existing = read(guest.getGuestId());
        if (existing == null) {
            return null; // controller turns this into a 404
        }
        validate(guest);
        Guest normalised = normaliseEmail(guest);
        String newEmail = normalised.getContactDetails().getEmail();
        String oldEmail = existing.getContactDetails() != null ? existing.getContactDetails().getEmail() : null;

        if (oldEmail == null || !oldEmail.equalsIgnoreCase(newEmail)) {
            if (hasOnlineAccount(oldEmail)) {
                throw new GuestConflictException(
                        "This guest has an online account, so the email can only be changed from that account");
            }
            if (emailBelongsToAnotherGuest(newEmail, existing.getGuestId())) {
                throw new GuestConflictException("Another guest already uses this email");
            }
        }
        return guestRepository.save(normalised);
    }

    // Mirrors the validation pattern used in PaymentService: real checks
    // wired into the path a live request actually takes, using the
    // already-existing Helper methods, thrown as plain IllegalArgumentException
    // (caught in GuestController and turned into a 400) rather than a custom
    // exception type.
    private void validate(Guest guest) {
        if (Helper.isNullOrEmpty(guest.getName())) {
            throw new IllegalArgumentException("Guest name is required");
        }
        if (Helper.isNullOrEmpty(guest.getName().getFirstName())
                || Helper.isNullOrEmpty(guest.getName().getLastName())) {
            throw new IllegalArgumentException("Guest first name and last name are required");
        }
        if (Helper.isNullOrEmpty(guest.getContactDetails())) {
            throw new IllegalArgumentException("Guest contact details are required");
        }
        String email = guest.getContactDetails().getEmail();
        if (Helper.isInvalidEmail(email == null ? null : email.trim())) {
            throw new IllegalArgumentException("A valid email is required");
        }
        if (Helper.isInvalidMobile(guest.getContactDetails().getMobile())) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits");
        }
    }

    // Emails are stored trimmed and in lowercase, so the same address typed
    // differently is still one guest.
    private Guest normaliseEmail(Guest guest) {
        ContactDetails contactDetails = new ContactDetails.Builder()
                .copy(guest.getContactDetails())
                .setEmail(guest.getContactDetails().getEmail().trim().toLowerCase(Locale.ROOT))
                .build();
        return new Guest.Builder().copy(guest).setContactDetails(contactDetails).build();
    }

    private boolean emailBelongsToAnotherGuest(String email, Long ownGuestId) {
        return guestRepository.findByContactDetails_EmailIgnoreCase(email)
                .stream()
                .anyMatch(other -> !other.getGuestId().equals(ownGuestId));
    }

    private boolean hasOnlineAccount(String email) {
        if (Helper.isNullOrEmpty(email)) {
            return false;
        }
        return appUserRepository.findByEmail(email).isPresent()
                || appUserRepository.findByEmail(email.trim().toLowerCase(Locale.ROOT)).isPresent();
    }

    @Override
    public boolean delete(Guest guest) {
        if (!bookingRepository.findByGuest_GuestId(guest.getGuestId()).isEmpty()) {
            throw new GuestConflictException("This guest has bookings, so the record is kept for the hotel's history");
        }
        if (guest.getContactDetails() != null && hasOnlineAccount(guest.getContactDetails().getEmail())) {
            throw new GuestConflictException("This guest has an online account, so the record can't be deleted");
        }
        guestRepository.delete(guest);
        return true;
    }

    @Override
    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Override
    public List<Guest> findByFirstName(String firstName) {
        return guestRepository.findByName_FirstName(firstName);
    }

    @Override
    public List<Guest> findByLastName(String lastName) {
        return guestRepository.findByName_LastName(lastName);
    }

    @Override
    public Guest findByEmail(String email) {
        if (Helper.isNullOrEmpty(email)) {
            return null;
        }
        return guestRepository.findByContactDetails_EmailIgnoreCase(email.trim())
                .stream().findFirst().orElse(null);
    }

    /* ==== DTO based methods, this is what GuestController actually calls ==== */

    public GuestDto createGuest(CreateGuestRequest request) {
        Guest guest = GuestMapper.toEntity(request);
        Guest saved = create(guest);
        return GuestMapper.toDto(saved);
    }

    public GuestDto readGuest(Long id) {
        Guest guest = read(id);
        if (guest == null) {
            return null;
        }
        return GuestMapper.toDto(guest);
    }

    public GuestDto updateGuest(UpdateGuestRequest request) {
        Guest updated = update(GuestMapper.toEntity(request));
        if (updated == null) {
            return null; // Controller returns 404 Not Found
        }
        return GuestMapper.toDto(updated);
    }

    public boolean deleteGuest(Long id) {
        Guest guest = read(id);
        if (guest == null) {
            return false;
        }
        return delete(guest);
    }

    public List<GuestDto> getAllGuests() {
        return findAll()
                .stream()
                .map(GuestMapper::toDto)
                .toList();
    }

    public List<GuestDto> getGuestsByFirstName(String firstName) {
        return findByFirstName(firstName)
                .stream()
                .map(GuestMapper::toDto)
                .toList();
    }

    public List<GuestDto> getGuestsByLastName(String lastName) {
        return findByLastName(lastName)
                .stream()
                .map(GuestMapper::toDto)
                .toList();
    }

    public GuestDto getGuestByEmail(String email) {
        Guest guest = findByEmail(email);
        if (guest == null) {
            return null;
        }
        return GuestMapper.toDto(guest);
    }
}
