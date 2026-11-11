package za.ac.cput.marginhotelmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.marginhotelmanagement.domain.dtos.auth.AuthUserResponse;
import za.ac.cput.marginhotelmanagement.domain.dtos.guest.UpdateContactRequest;
import za.ac.cput.marginhotelmanagement.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/contact")
    public ResponseEntity<?> updateContact(@RequestBody UpdateContactRequest request,
                                           Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            // The email comes from the verified token, never from the request body
            AuthUserResponse response = accountService.updateContact(authentication.getName(), request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}