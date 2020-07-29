/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.service;

import artem.nikname.demoSQLSpringHTML.model.Globine;
import artem.nikname.demoSQLSpringHTML.model.Patient;
import artem.nikname.demoSQLSpringHTML.model.Poltava;
import artem.nikname.demoSQLSpringHTML.repository.GlobineRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import artem.nikname.demoSQLSpringHTML.repository.PatientRepository;
import artem.nikname.demoSQLSpringHTML.repository.PoltavaRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Master
 */
@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository repository = null;
    private final PoltavaRepository poltavaRepository;
    private final GlobineRepository globineRepository;

    @Autowired
    public PatientServiceImpl(PoltavaRepository poltavaRepository,
            GlobineRepository globineRepository) {
        this.poltavaRepository = poltavaRepository;
        this.globineRepository = globineRepository;
    }

    @Override
    public List<Patient> findAll(String tableName) {
        setRepository(tableName);
        return repository.findAll();
    }

    @Override
    public Patient save(int reportNumber, String surname, String name, String fathersName,
            String sex, String birthDate, String deathDate, String expert, String tableName) {
        setRepository(tableName);
        Patient patient = setPatient(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert, tableName);
        if (repository == null) {
            return null;
        }
        return repository.save(patient);
    }

    @Override
    public Patient save(Patient patient, String tableName) {
        setRepository(tableName);
        if (repository == null) {
            return null;
        }
        return repository.save(patient);
    }

    @Override
    public List<Patient> getPatientByName(String name, String tableName) {
        setRepository(tableName);
        if (repository == null) {
            return null;
        }
        return repository.getPatientByName(name);
    }

    @Override
    public Patient getPatientById(int id, String tableName) {
        setRepository(tableName);
        if (repository == null) {
            return null;
        }
        Patient patient = repository.getPatientById(id);
        patient.setBirthDate(Patient.dateForInputDate(patient.getBirthDate()));
        patient.setDeathDate(Patient.dateForInputDate(patient.getDeathDate()));
        return patient;

    }

    @Override
    public int updatePatient(int reportNumber, String name, String surname, String fathersName,
            String sex, String birthDate, String deathDate, String expert, int id, String tableName) {
        setRepository(tableName);
        if (repository == null) {
            return 0;
        }
        return repository.updatePatient(reportNumber, name, surname, fathersName, sex,
                birthDate, deathDate, expert, id);
    }

    private void setRepository(String tableName) {

        switch (tableName) {
            case "poltava":
                repository = poltavaRepository;
                break;
            case "globine":
                repository = globineRepository;
                break;
            default:
                repository = null;
        }
    }

    public Patient setPatient(int reportNumber, String surname, String name, String fathersName,
            String sex, String birthDate, String deathDate, String expert, String tableName) {
        Patient patient = null;
        switch (tableName) {
            case "poltava":
                patient = new Poltava(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert);
                break;
            case "globine":
                patient = new Globine(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert);
                break;
        }
        return patient;
    }

    @Override
    public int deletePatient(int patientId, String tableName) {
        setRepository(tableName);
        return repository.deletePatient(patientId);
    }

    @Override
    public void resetPK(String tableName) {
        setRepository(tableName);
        repository.resetPK();
    }

}
