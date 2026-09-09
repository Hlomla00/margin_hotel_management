package za.ac.cput.marginhotelmanagement.controller;
/*
   GuestController.java
   REST controller for Guest entity
   Author: Hlomla Magopeni (218070349)
   Date: 21 August 2026
   Updated: 09 September 2026 — rewritten against the DTO-based contract
   (CreateGuestRequest / GuestDto / UpdateGuestRequest), same pattern as
   BookingController/PaymentController.
   */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.dtos.CreateGuestRequest;
import za.ac.cput.marginhotelmanagement.dtos.GuestDto;
import za.ac.cput.marginhotelmanagement.dtos.UpdateGuestRequest;
import za.ac.cput.marginhotelmanagement.service.GuestService;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateGuestRequest request) {
        try {
            GuestDto createdGuest = guestService.createGuest(request);
            return new ResponseEntity<>(createdGuest, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = guestService.deleteGuest(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
    public ResponseEntity<GuestDto> findByEmail(@PathVariable String email) {
        GuestDto guest = guestService.getGuestByEmail(email);
        if (guest != null) {
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
