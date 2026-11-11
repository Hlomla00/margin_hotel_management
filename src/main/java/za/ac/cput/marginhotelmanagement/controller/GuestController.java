package za.ac.cput.marginhotelmanagement.controller;
/*
   GuestController.java
   REST controller for Guest entity
   Author: Hlomla Magopeni (218070349)
   Date: 21 August 2026
   Updated: 09 September 2026 — rewritten against the DTO-based contract
   (CreateGuestRequest / GuestDto / UpdateGuestRequest), same pattern as
   BookingController/PaymentController.
   Updated: 04 November 2026 — 409 Conflict for guest rule clashes: a duplicate
   email returns the existing guest so the front desk books under that guest;
   deleting a guest with bookings or an online account, and changing the
   email of a guest with an online account, explain why they were refused.
   */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.GuestDto;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.UpdateGuestRequest;
import za.ac.cput.marginhotelmanagement.service.GuestService;
import za.ac.cput.marginhotelmanagement.service.GuestFlowAccess;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private final GuestService guestService;
    private final GuestFlowAccess guestFlowAccess;

    @Autowired
    public GuestController(GuestService guestService, GuestFlowAccess guestFlowAccess) {
        this.guestService = guestService;
        this.guestFlowAccess = guestFlowAccess;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateGuestRequest request, Authentication authentication) {
        if (guestFlowAccess.isGuest(authentication)) {
            if (!guestFlowAccess.ownsEmail(request.getEmail(), authentication)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (guestService.getGuestByEmail(request.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        try {
            GuestDto createdGuest = guestService.createGuest(request);
            return new ResponseEntity<>(createdGuest, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (GuestService.GuestConflictException e) {
            // Return the guest who already has this email, so the booking can use them.
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("message", e.getMessage());
            body.put("existingGuest", guestService.getGuestByEmail(request.getEmail()));
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<GuestDto> read(@PathVariable Long id) {
        GuestDto guest = guestService.readGuest(id);
        if (guest != null) {
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateGuestRequest request) {
        try {
            GuestDto updated = guestService.updateGuest(request);
            if (updated == null) {
                return new ResponseEntity<>("No guest found with ID #" + request.getGuestId(), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (GuestService.GuestConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            boolean deleted = guestService.deleteGuest(id);
            if (!deleted) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (GuestService.GuestConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<GuestDto>> getAll() {
        List<GuestDto> guests = guestService.getAllGuests();
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/findByFirstName/{firstName}")
    public ResponseEntity<List<GuestDto>> findByFirstName(@PathVariable String firstName) {
        List<GuestDto> guests = guestService.getGuestsByFirstName(firstName);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/findByLastName/{lastName}")
    public ResponseEntity<List<GuestDto>> findByLastName(@PathVariable String lastName) {
        List<GuestDto> guests = guestService.getGuestsByLastName(lastName);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<GuestDto> findByEmail(@PathVariable String email, Authentication authentication) {
        if (guestFlowAccess.isGuest(authentication) && !guestFlowAccess.ownsEmail(email, authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        GuestDto guest = guestService.getGuestByEmail(email);
        if (guest != null) {
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
