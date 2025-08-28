package dev.pm.patientservice.mapper;

import dev.pm.patientservice.dto.PatientRequestDTO;
import dev.pm.patientservice.dto.PatientResponseDTO;
import dev.pm.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId().toString());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setDateOfBirth(patient.getDateOfBirth().toString());
        return patientResponseDTO;
    }

    public static Patient toPatient(PatientRequestDTO PatientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(PatientRequestDTO.getName());
        patient.setAddress(PatientRequestDTO.getAddress());
        patient.setEmail(PatientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(PatientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(PatientRequestDTO.getRegisteredDate()));

        return patient;
    }
}
