/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.controller;

import artem.nikname.demoSQLSpringHTML.model.IdClass;
import artem.nikname.demoSQLSpringHTML.model.Patient;
import artem.nikname.demoSQLSpringHTML.model.User;
import artem.nikname.demoSQLSpringHTML.security.SecurityCheck;
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
import artem.nikname.demoSQLSpringHTML.repository.UsersRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Master
 */
@Controller
@RequestMapping("/patient")
public class PatientController {

    public static final Set<User> users = new HashSet<>();

    private final PatientService patientService;
    private SecurityCheck securityCheck;
    private final UsersRepository usersRepository;
    private final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    public PatientController(
            SecurityCheck securityCheck,
            PatientService patientService,
            UsersRepository usersRepositiry) {
        this.usersRepository = usersRepositiry;
        this.patientService = patientService;
        this.securityCheck = securityCheck;
    }

    @ModelAttribute("user")
    public User getUser() {
        return new User();
    }

    @ModelAttribute("patient")
    public Patient getPatient() {
        return new Patient();
    }

    @ModelAttribute("idClass")
    public IdClass getIdClass() {
        return new IdClass();
    }

    @PostMapping("add")
    public String processAdd(@ModelAttribute("idClass") IdClass idClass, @RequestParam int reportNumber, String surname, String name, String fathersName,
            String sex, String birthDate, String deathDate, String expert, Model model) {
        System.out.println("In Post Add");
        User user = usersRepository.getUserById(idClass.getId());
        System.out.println("User from Model Attribute = " + user);
        System.out.println("User from POST Add:" + user);
        if (user == null) {
            return "redirect:/login";
        } else {
            logger.info("add post method mapping");
//        Patient patients = new Patient(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert);
            model.addAttribute("massege", "Додавання в базу");
            model.addAttribute("id", idClass.getId());
            synchronized (patientService) {
                if (patientService.save(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert, user.getTableName()) != null) {
                    return "success-form";
                }
            }
            return "fail";
        }
    }

    @GetMapping("addNew")
    public String addNewPatient(@ModelAttribute("IdClass") IdClass idClass, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
        model.addAttribute("user", user);
        model.addAttribute("id", user.getId());
        model.addAttribute("currentDate", getCurrentDate());
        return "add-form";
        }
    }

    

    @GetMapping("success")
    public String success(Model model, @RequestParam int id) {
        System.out.println("Success Post");
        System.out.println("");

        model.addAttribute("massege", "ededed id = " + id);
//        if (user == null) {
//            return "/";
//        }
        logger.info("success method mapping");
        return "success";
    }

    @GetMapping("home")
    public String home(@ModelAttribute("idClass") IdClass idClass,Model model) {
        model.addAttribute("id", idClass.getId());
        return "home";
    }

    @GetMapping("find_by_name_rez")
    public String find(@ModelAttribute("idClass") IdClass idClass, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            System.out.println("find method");
            List<Patient> list = null;
            List<Patient> patients = new ArrayList<>();
            model.addAttribute("nullList", list);
            model.addAttribute("patients", patients);
            model.addAttribute("user", user);
            model.addAttribute("id", idClass.getId());
            return "find_by_name_rez_form";
        }
    }
    
    @GetMapping("searchReasult")
    public String findConfirmCancel(@ModelAttribute("idClass") IdClass idClass, @RequestParam String searchName, Model model) {
        return findProcess(idClass, searchName, model);
    }

    @PostMapping("searchReasult")
    public String findProcess(@ModelAttribute("idClass") IdClass idClass, @RequestParam String name, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            List<Patient> list = new ArrayList<>();
            List<Patient> patients = patientService.getPatientByName(name, user.getTableName());
            if (patients != null) {
                for (Patient patient : patients) {
                    System.out.println(patient);
                }
                model.addAttribute("id", user.getId());
                model.addAttribute("searchName", name);
                model.addAttribute("nullList", list);
                model.addAttribute("patients", patients);
            }
            return "find_by_name_rez_form";
        }
    }

    @GetMapping("fill_db")
    public String fillDatabase(@RequestParam String tableName,int id,Model model) throws IOException {

        try {
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\Master\\Desktop\\morgPoltava2020.txt"), "UTF-8"));
            String line = "";
            int records = 0;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(line.split(" ")));
                int size = arr.size();
                String res = arr.get(size - 2) + " " + arr.get(size - 1);
                arr.set(size - 2, res);
                arr.remove(size - 1);
                synchronized (patientService) {
                    patientService.save(Integer.parseInt(arr.get(0)), arr.get(1), arr.get(2), arr.get(3), arr.get(4),
                            Patient.dateForInputDate(replaceDots(arr.get(5))), Patient.dateForInputDate(replaceDots(arr.get(6))), arr.get(7), tableName);
                }
                System.out.println("executed");
                records++;
            }
            model.addAttribute("records", records);
            model.addAttribute("message", "Додавання");
            model.addAttribute("id", id);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fill-db-form";
    }

    private String replaceDots(String date) {
        return date.replace(".", "-");
    }

    @GetMapping("edit")
    public String edit(@ModelAttribute ("idClass") IdClass idClass,@RequestParam int patientId, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            Patient patient = patientService.getPatientById(patientId, user.getTableName());
            model.addAttribute("id", user.getId());
            model.addAttribute("patient", patient);
            model.addAttribute("currentDate", getCurrentDate());
            System.out.println("patient = " + patient);
            return "edit-form";
        }
    }

    @PostMapping("edit")
    public String processingEdit(@RequestParam int reportNumber, String surname, String name,
            String fathersName, String sex, String birthDate,
            String deathDate, String expert, int patientId, int id, Model model) {
        User user = usersRepository.getUserById(id);
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            model.addAttribute("id", id);
            model.addAttribute("massege", "Редагування");
            String tableName = user.getTableName();
            birthDate = Patient.dateToViewMode(birthDate);
            deathDate = Patient.dateToViewMode(deathDate);
            int rez = patientService.updatePatient(reportNumber, name, surname,
                    fathersName, sex, birthDate, deathDate, expert, patientId, tableName);
            if (rez < 1) {
                return "fail-form";
            }
            return "success-form";
        }
    }

    @GetMapping("all")
    public String findAll(@RequestParam Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("user", user);
//        if (user == null) {
//            return "/";
//        } else {
        List<Patient> patients = patientService.findAll(user.getTableName());
        model.addAttribute("patients", patients);
        return "showUsers";
//        }
    }

    @GetMapping("exit")
    public String exit(@RequestParam int id) {
        User user = usersRepository.getUserById(id);
        if (user != null) {
            if (users.contains(user)) {
                users.remove(user);
            }
        }
        return "redirect:/showLoginForm";
    }

    public static String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(currentDate);
    }
    
    @GetMapping("delete")
    public String delete(@RequestParam int id,String searchName,int patientId,Model model){
         User user = usersRepository.getUserById(id);
        String tableName = user.getTableName();
        patientService.deletePatient(patientId, tableName);
        model.addAttribute("patients", patientService.getPatientByName(searchName, tableName));
        model.addAttribute("name", searchName);
        model.addAttribute("patientId", patientId);
        model.addAttribute("id", id);
         
         return "find_by_name_rez_form";
    }

}
