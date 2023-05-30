package com.example.healthsystem.service;

import com.example.healthsystem.dto.PatientDTO;
import com.example.healthsystem.entity.Patient;
import com.example.healthsystem.repository.PatientRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return patients.stream()
                .map( patient -> PatientDTO.builder()
                        .id(patient.getId())
                        .name(patient.getName())
                        .bmi(patient.getBmi())
                        .age(patient.getAge())
                        .birthDate(formatter.format(patient.getBirthDate()))
                        .build()).collect(Collectors.toList());
    }

    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient != null) {
            return PatientDTO.builder()
                    .id(patient.getId())
                    .name(patient.getName())
                    .bmi(patient.getBmi())
                    .age(patient.getAge())
                    .birthDate(patient.getBirthDate().toString())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient not found");
        }
    }

    public PatientDTO createPatient(PatientDTO patientDTO) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Patient patient = Patient.builder()
                .id(patientDTO.getId())
                .name(patientDTO.getName())
                .bmi(patientDTO.getBmi())
                .age(patientDTO.getAge())
                .birthDate(formatter.parse(patientDTO.getBirthDate()))
                .build();
        Patient savedPatient = patientRepository.save(patient);
        return PatientDTO.builder()
                .id(savedPatient.getId())
                .name(savedPatient.getName())
                .age(savedPatient.getAge())
                .bmi(savedPatient.getBmi())
                .birthDate(savedPatient.getBirthDate().toString())
                .build();
    }

    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) throws ParseException {
        Patient existingPatient = patientRepository.findById(id).orElse(null);
        if(isValidBirthDate(patientDTO.getBirthDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid date format");
        }
        if (existingPatient != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            existingPatient.setName(patientDTO.getName());
            existingPatient.setBmi(patientDTO.getBmi());
            existingPatient.setAge(patientDTO.getAge());
            existingPatient.setBirthDate(formatter.parse(patientDTO.getBirthDate()));
            Patient updatedPatient = patientRepository.save(existingPatient);

            return PatientDTO.builder()
                    .id(updatedPatient.getId())
                    .name(updatedPatient.getName())
                    .bmi(updatedPatient.getBmi())
                    .age(updatedPatient.getAge())
                    .birthDate(updatedPatient.getBirthDate().toString())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient not found");
        }
    }

    public boolean isValidBirthDate(String birthDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            formatter.parse(birthDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient not found");
        }
    }
}
