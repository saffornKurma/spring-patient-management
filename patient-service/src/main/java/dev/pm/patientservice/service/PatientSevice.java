package dev.pm.patientservice.service;


import dev.pm.patientservice.dto.PatientRequestDTO;
import dev.pm.patientservice.dto.PatientResponseDTO;
import dev.pm.patientservice.exception.EmailAlreadyExistsException;
import dev.pm.patientservice.exception.PatientNotFoundException;
import dev.pm.patientservice.grpc.BillingServiceGrpcClient;
import dev.pm.patientservice.kafka.KafkaProducer;
import dev.pm.patientservice.mapper.PatientMapper;
import dev.pm.patientservice.model.Patient;
import dev.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientSevice {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientSevice(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).collect(Collectors.toList());
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists :"+patientRequestDTO.getEmail());
        }
        Patient patient=PatientMapper.toPatient(patientRequestDTO);
        patientRepository.save(patient);

        //this is grpc request to billingService
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),patient.getName(),patient.getEmail());

        //this sending to kafka for notification service
        kafkaProducer.sendEvent(patient);

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        Patient patient =patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("Patient not found with ID:"+id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new PatientNotFoundException("A patient with this email already exists : :"+patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));


        return PatientMapper.toDTO(patientRepository.save(patient));

    }

    public PatientResponseDTO deletePatient(UUID id) {
        patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("Patient not found with ID:"+id));

        PatientResponseDTO patientResponseDTO=PatientMapper.toDTO(patientRepository.findById(id).get());

        patientRepository.deleteById(id);

        return patientResponseDTO;
    }
}
