/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.repository;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import artem.nikname.demoSQLSpringHTML.model.Poltava;
import artem.nikname.demoSQLSpringHTML.model.SearchClass;
import java.util.List;
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
public interface PoltavaRepository extends PatientRepository,JpaRepository<Patient,Integer>{
    
    String TABLE = "Poltava";
     
    @Override
    @Query(value = "SELECT p FROM " + TABLE + " p WHERE p.name LIKE ?1% "
                + "OR p.surname LIKE ?1% ORDER BY p.surname")
    List<Patient> getPatientByName(String name);
    
    @Override
    @Query(value = "SELECT p FROM " + TABLE + " p WHERE p.name LIKE ?1% AND p.surname LIKE ?2% "
            + "AND p.examDate BETWEEN STR_TO_DATE(?3,'%Y,%m,%d') AND STR_TO_DATE(?4,'%Y,%m,%d')")
    List<Patient> getPatientByNameSurnameYear(String name,String surname,String yearFrom,String yearTo);

    @Override
    @Query(value = "SELECT p FROM " + TABLE + " p WHERE p.id = ?1")
    Patient getPatientById(int id);

    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE " + TABLE + " AS p SET p.report_number = ?, p.name = ?, "
            + "p.surname = ?, p.fathers_name = ?, p.sex = ?, p.year_of_birth = ?, "
            + "p.exam_date = ?, p.expert = ? WHERE p.patient_id = ?",
            nativeQuery = true)
    int updatePatient(int reportNumber, String name, String surname, String fathersName,
            String sex, String yearOfBirth, String examDate, String expert, int patientId);

    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "DELETE FROM " + TABLE + " WHERE patient_id = ?",
            nativeQuery = true)
    int deletePatient(int patientId);
    
    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE hibernate_sequence SET next_val = 1",
            nativeQuery = true)
    void resetPK();
}
