
package artem.nikname.demoSQLSpringHTML.service;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import java.util.List;


public interface PatientService {
    
    List<Patient> findAll();
    Patient save(Patient patient);
    List<Patient> getPatientByName(String table,String name);
    Patient getPatientById(String table,int id);
    int updatePatient(String table,int reportNumber,String name,String surname,String fathersName,
            String sex,String birthDate,String deathDate,String expert,int id);
    
}
