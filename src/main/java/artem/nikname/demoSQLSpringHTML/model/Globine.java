/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Master
 */
@Entity(name = "Globine")
@Table(name = "globine")
public class Globine extends Patient{

    public Globine() {
    }

    public Globine(int reprtNumber, String surname, String name, 
            String fathersName, String sex, String yearOfBirth, 
            LocalDate examDate, String expert) {
        super(reprtNumber, surname, name, fathersName, sex, yearOfBirth, examDate, expert);
    }

   
    
    
    
    
}
