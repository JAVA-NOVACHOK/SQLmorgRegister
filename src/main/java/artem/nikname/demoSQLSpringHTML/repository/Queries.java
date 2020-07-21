/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.repository;

/**
 *
 * @author Master
 */
public final class Queries {
    
    public static String getPatientByName(String table){
        return "SELECT p FROM " + table + " p WHERE p.name LIKE ?1% "
                + "OR p.surname LIKE ?1% ORDER BY p.surname";
    }
    
    public static String getPatientById(int id){
        return "SELECT p FROM " + id + " p WHERE p.id = ?1";
    }
    
    public static String updatePatient(String table){
        return "UPDATE " + table + " AS p SET p.report_number = ?, p.name = ?, "
            + "p.surname = ?, p.fathers_name = ?, p.sex = ?, p.birth_date = ?, "
            + "p.death_date = ?, p.expert = ? WHERE p.id = ?";
    }
    

    private Queries(){}
    
}
