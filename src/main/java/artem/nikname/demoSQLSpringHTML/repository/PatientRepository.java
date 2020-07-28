/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.repository;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import java.util.List;
import java.util.jar.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Master
 */

public interface PatientRepository {
    
    
    List<Patient> getPatientByName(String name);

    
    Patient getPatientById(int id);

   
    int updatePatient(int reportNumber, String name, String surname, String fathersName,
            String sex, String birthDate, String deathDate, String expert, int id);
    
    Patient save(Patient p);
    
    public List<Patient> findAll();
    
    int deletePatient(int patientId);
    void resetPK();

}
