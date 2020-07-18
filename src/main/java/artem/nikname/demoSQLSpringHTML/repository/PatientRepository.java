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
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    
     //ADD TABLE ARGUMENT IN THE SERVICE CLASS
    //ADD MORE SERVICE CLASSES
    @Query("SELECT p FROM ?1 p WHERE p.name LIKE ?2% OR p.surname LIKE ?2% "
            + "ORDER BY p.surname")
    List<Patient> getPatientByName(String table,String name);

    @Query("SELECT p FROM ?1 p WHERE p.id = ?2")
    Patient getPatientById(String table,int id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ? AS p SET p.report_number = ?, p.name = ?, "
            + "p.surname = ?, p.fathers_name = ?, p.sex = ?, p.birth_date = ?, "
            + "p.death_date = ?, p.expert = ? WHERE p.id = ?",
            nativeQuery = true)
    int updatePatient(String table,int reportNumber, String name, String surname, String fathersName,
            String sex, String birthDate, String deathDate, String expert, int id);

}
