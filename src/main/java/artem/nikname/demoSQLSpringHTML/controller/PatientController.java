/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.controller;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import artem.nikname.demoSQLSpringHTML.util.MappingNames;
import artem.nikname.demoSQLSpringHTML.util.ViewNames;
import com.sun.istack.NotNull;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import artem.nikname.demoSQLSpringHTML.service.PatientService;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Master
 */
@Controller
public class PatientController {

    private final PatientService patientService;
    private final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    @GetMapping("signup")
    public String signUp(){
        return "signup";
    }

    @GetMapping("all")
    public String findAll(Model model) {

        List<Patient> patients = patientService.findAll();

        model.addAttribute("patients", patients);

        return "showUsers";
    }
    
    private String getCurrentDate(){
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(currentDate);
    }

    @GetMapping(MappingNames.ADD)
    public String add(Model model) {
        model.addAttribute("currentDate",getCurrentDate());
        logger.info("add method mapping");
        return ViewNames.ADD;
    }

    @PostMapping(ViewNames.ADD)
    public String processAdd(@RequestParam @NotNull int reportNumber, @NotNull String surname, @NotNull String name,
            @NotNull String fathersName, @NotNull String sex, @NotNull String birthDate,
            @NotNull String deathDate, @NotNull String expert,Model model) {
        logger.info("add post method mapping");
        Patient patients = new Patient(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert);
        model.addAttribute("massege", "Додавання в базу");
        if (patientService.save(patients) != null) {
            return ViewNames.SUCCESS;
        }
        return ViewNames.FAIL;
    }

    @PostMapping(ViewNames.FAIL)
    public String failAdd(@RequestParam Model model) {

        logger.info("fail method mapping");
        return ViewNames.FAIL;
    }

    @PostMapping(ViewNames.SUCCESS)
    public String success(@RequestParam Model model) {
        logger.info("success method mapping");
        return "success";
    }

    @GetMapping("home")
    public String home() {
        return "home";
    }

    @GetMapping("find_by_name_rez")
    public String find(Model model) {
        System.out.println("find method");
        List<Patient> list = null;
        List<Patient> patients = new ArrayList<>();
        model.addAttribute("nullList", list);
        model.addAttribute("patients", patients);
        return "find_by_name_rez";
    }

    @PostMapping("find_by_name_rez")
    public String findProcess(@RequestParam String name, String table, Model model) {
        System.out.println("In Post find by name");
        List<Patient> list = new ArrayList<>();
        model.getAttribute(name);
        List<Patient> patients = patientService.getPatientByName(table,name);
        if (patients != null) {
            for (Patient patient : patients) {
                System.out.println(patient);
            }
            model.addAttribute("nullList", list);
            model.addAttribute("patients", patients);
        }
        return "find_by_name_rez";
    }

    @GetMapping("fill_db")
    public void fillDatabase() throws IOException {

        try {

            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Master\\Desktop\\morgPoltava2020.txt"), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(line.split(" ")));
                int size = arr.size();
                String res = arr.get(size - 2) + " " + arr.get(size - 1);
                arr.set(size - 2, res);
                arr.remove(size - 1);
                Patient patient = new Patient(Integer.parseInt(arr.get(0)), arr.get(1), arr.get(2), arr.get(3), arr.get(4),
                        replaceDots(arr.get(5)), replaceDots(arr.get(6)), arr.get(7));
                synchronized (patientService) {
                    patientService.save(patient);
                }
                System.out.println("executed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String replaceDots(String date){
        return date.replace(".","-");
    }

    @GetMapping("edit")
    public String edit(@RequestParam int id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("currentDate", getCurrentDate());
        System.out.println("patient = " + patient);
        return "edit";
    }

    @PostMapping("edit")
    public String processingEdit(@RequestParam @NotNull int reportNumber, @NotNull String surname, @NotNull String name,
            @NotNull String fathersName, @NotNull String sex, @NotNull String birthDate,
            @NotNull String deathDate, @NotNull String expert, int id,Model model) {
        model.addAttribute("massege", "Редагування");
        
        birthDate = Patient.dateToViewMode(birthDate);
        deathDate = Patient.dateToViewMode(deathDate);
//        int rez = patientService.updatePatient(reportNumber,name, surname,
                fathersName, sex, birthDate, deathDate, expert, id);
        if (rez < 1) {
            return ViewNames.FAIL;
        }
        return ViewNames.SUCCESS;
    }
}
