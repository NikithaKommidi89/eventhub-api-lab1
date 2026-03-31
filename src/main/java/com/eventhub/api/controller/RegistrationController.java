package com.eventhub.api.controller;

import com.eventhub.api.dto.RegistrationDTO;
import com.eventhub.api.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@Tag(name = "Registrations", description = "Event registration endpoints")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    @Operation(summary = "Get all registrations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved registrations")
    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations() {
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get registration by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration found"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    public ResponseEntity<RegistrationDTO> getRegistrationById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get registrations by user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user registrations")
    public ResponseEntity<List<RegistrationDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(registrationService.getRegistrationsByUser(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new registration")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registration created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "User or Event not found")
    })
    public ResponseEntity<RegistrationDTO> createRegistration(@Valid @RequestBody RegistrationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.createRegistration(dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update registration status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    public ResponseEntity<RegistrationDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(registrationService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a registration")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registration deleted"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }
}
