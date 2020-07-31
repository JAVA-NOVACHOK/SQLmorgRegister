package artem.nikname.demoSQLSpringHTML.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Poltava")
@Table(name = "poltava")
public class Poltava extends Patient {

    public Poltava() {
    }

    public Poltava(int reprtNumber, String surname, String name,
            String fathersName, String sex, String yearOfBirth,
            String examDate, String expert) {
        super(reprtNumber, surname, name, fathersName, sex, yearOfBirth,
                examDate, expert);
    }

}
