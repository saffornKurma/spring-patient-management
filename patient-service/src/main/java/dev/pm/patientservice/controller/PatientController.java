package dev.pm.patientservice.controller;


import dev.pm.patientservice.dto.PatientRequestDTO;
import dev.pm.patientservice.dto.PatientResponseDTO;
import dev.pm.patientservice.dto.validators.CreatePatientValidationGroup;
import dev.pm.patientservice.service.PatientSevice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient",description = "API for managing patients")
public class PatientController {

    private final PatientSevice patientSevice;

    PatientController(PatientSevice patientSevice) {
        this.patientSevice = patientSevice;
    }

    @GetMapping
    @Operation(summary = "Get patients")//@operation is for swagger it shows what the api does
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientSevice.getPatients();
        return ResponseEntity.ok().body(patients);
    }
//here validated is different
    //because here registeredDate is required
    //@Validated({Default.class, CreatePatientValidationGroup.class})
    @PostMapping
    @Operation(summary = "create patients")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        System.out.println("log:"+patientRequestDTO.getName());
        System.out.println("log:"+patientRequestDTO.getAddress());
        System.out.println("log:"+patientRequestDTO.getEmail());
        System.out.println("log:"+patientRequestDTO.getDateOfBirth());
        System.out.println("log:"+patientRequestDTO.getRegisteredDate());
        PatientResponseDTO patientResponseDTO = patientSevice.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

//@Validated({Default.class}) is used for ignoring the validation for update
    @PutMapping("/{id}")
    @Operation(summary = "Update patients")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO= patientSevice.updatePatient(id,patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patients")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        PatientResponseDTO patientResponseDTO = patientSevice.deletePatient(id);
        return ResponseEntity.noContent().build();
    }



}
