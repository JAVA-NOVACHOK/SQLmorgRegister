/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.service;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import artem.nikname.demoSQLSpringHTML.repository.PatientRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Master
 */
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final String TABLE = "patients";

    @Autowired
    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Patient> findAll() {
        return repository.findAll();
    }

    @Override
    public Patient save(Patient patient) {

        return repository.save(patient);
    }

    @Override
    public List<Patient> getPatientByName(String name,String table) {
        return repository.getPatientByName(name,table);
    }

    @Override
    public Patient getPatientById(String table,int id) {
        Patient patient = repository.getPatientById(table,id);
        patient.setBirthDate(Patient.dateForInputDate(patient.getBirthDate()));
        patient.setDeathDate(Patient.dateForInputDate(patient.getDeathDate()));
        return patient;

    }

    @Override
    public int updatePatient(String table,int reportNumber, String name, String surname, String fathersName,
            String sex, String birthDate, String deathDate, String expert, int id) {
        return repository.updatePatient(table,reportNumber, name, surname, fathersName, sex,
                birthDate, deathDate, expert, id);
    }

}
