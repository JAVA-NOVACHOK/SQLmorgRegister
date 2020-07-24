package artem.nikname.demoSQLSpringHTML.service;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import java.util.List;

public interface PatientService {

    List<Patient> findAll(String tableName);

    Patient save(int reportNumber, String surname, String name, String fathersName,
            String sex, String birthDate, String deathDate, String expert, String tableName);
    Patient save(Patient patient, String tableName);

    List<Patient> getPatientByName(String name, String tableName);

    Patient getPatientById(int id, String tableName);

    Patient setPatient(int reportNumber, String name, String surname, String fathersName,
            String sex, String birthDate, String deathDate, String expert,String tableName);

    int updatePatient(int reportNumber, String name, String surname, String fathersName,
            String sex, String birthDate, String deathDate, String expert, int id, String tableName);

}
