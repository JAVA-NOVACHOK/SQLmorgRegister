/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

/**
 *
 * @author Master
 */
@MappedSuperclass
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int patientId;
    private int reportNumber;
    private String surname;
    private String name;
    private String fathersName;
    private String sex;
    private String yearOfBirth;
    private String examDate;
    private String expert;

    public Patient() {
    }

    public Patient(int reprtNumber, String surname, String name, String fathersName, String sex, String yearOfBirth, String examDate, String expert) {
        this.surname = surname;
        this.reportNumber = reprtNumber;
        this.name = name;
        this.fathersName = fathersName;
        this.sex = sex;
        this.yearOfBirth = yearOfBirth;
        this.examDate = dateToViewMode(examDate);
        this.expert = expert;
    }

    public static String dateToViewMode(String inputDate) {
        LocalDate date = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static String dateForInputDate(String viewDate) {
        LocalDate date = LocalDate.parse(viewDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getYearOfBirth() {
        if (yearOfBirth.equals("")) {
            System.out.println("yearOfBirth = 0");
            return "0";
        }
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.patientId;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Patient other = (Patient) obj;
        if (this.patientId != other.patientId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Patient{" + "patientId=" + patientId + ", reportNumber="
                + reportNumber + ", surname=" + surname + ", name=" + name
                + ", fathersName=" + fathersName + ", sex=" + sex
                + ", yearOfBirth=" + yearOfBirth + ", examDate="
                + examDate + ", expert=" + expert + '}';
    }

}
