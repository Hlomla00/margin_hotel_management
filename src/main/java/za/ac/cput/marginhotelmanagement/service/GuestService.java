package za.ac.cput.marginhotelmanagement.service;
/*
    GuestService.java
    Service implementation for Guest entity
    Author: Hlomla Magopeni (218070349)
    Date: 16 July 2026
    Updated: 09 September 2026 — added the DTO-based create/read/update/delete
    methods (CreateGuestRequest / GuestDto / UpdateGuestRequest) that
    GuestController now calls, same pattern as PaymentService/BookingService.
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.dtos.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.dtos.GuestDto;
import za.ac.cput.marginhotelmanagement.dtos.UpdateGuestRequest;
import za.ac.cput.marginhotelmanagement.mappers.GuestMapper;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.util.List;

@Service
public class GuestService implements IGuestService {

    private GuestRepository guestRepository;

    @Autowired
    GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest create(Guest guest) {
        validate(guest);
        return guestRepository.save(guest);
    }

    @Override
    public Guest read(Long id) {
        return guestRepository.findById(id).orElse(null);
    }

    @Override
    public Guest update(Guest guest) {
        validate(guest);
        if (guest.getGuestId() == null || !guestRepository.existsById(guest.getGuestId())) {
            return null; // controller turns this into a 404
        }
        return guestRepository.save(guest);
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
        if (Helper.isInvalidEmail(guest.getContactDetails().getEmail())) {
            throw new IllegalArgumentException("A valid email is required");
        }
        if (Helper.isInvalidMobile(guest.getContactDetails().getMobile())) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits");
        }
    }

    @Override
    public boolean delete(Guest guest) {
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
        return guestRepository.findByContactDetails_Email(email);
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
        if (Helper.isNullOrEmpty(request.getGuestId()) || !guestRepository.existsById(request.getGuestId())) {
            return null; // Controller returns 404 Not Found
        }
        Guest guest = GuestMapper.toEntity(request);
        Guest updated = update(guest);
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