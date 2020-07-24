/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.controller;

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

//    @GetMapping("/add")
//    public String add(Model model, @RequestParam String login, String psw) {
//        System.out.println("In Get Add");
//        User user = null;
//        if (securityCheck.checkForInjection(login, psw)) {
//            user = usersRepository.getUserByLoginAndPassword(login, psw);
//            System.out.println("User from POST signup: " + user + " login:" + login + " psw:" + psw);
//        }
//        if (user == null) {
//            model.addAttribute("massege", "1");
//            return "/login";
//        } else {
//            users.add(user);
//        }
//        System.out.println("User from GET Add:" + user);
//        model.addAttribute("currentDate", getCurrentDate());
//        model.addAttribute("user", user);
//        logger.info("add method mapping");
//        return "addForm";
//    }
    @PostMapping("add")
    public String processAdd(@RequestParam int id, int reportNumber, String surname, String name, String fathersName,
            String sex, String birthDate, String deathDate, String expert, Model model) {
        System.out.println("In Post Add");
        User user = usersRepository.getUserById(id);
        System.out.println("User from Model Attribute = " + user);
        System.out.println("User from POST Add:" + user);
        if (user == null) {
            return "redirect:/login";
        } else {
            logger.info("add post method mapping");
//        Patient patients = new Patient(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert);
            model.addAttribute("massege", "Додавання в базу");
            model.addAttribute("id", id);
            synchronized (patientService) {
                if (patientService.save(reportNumber, surname, name, fathersName, sex, birthDate, deathDate, expert, user.getTableName()) != null) {
                    return "success-form";
                }
            }
            return "fail";
        }
    }

    @GetMapping("addNew")
    public String addNewPatient(Model model, @RequestParam int id) {
        User user = usersRepository.getUserById(id);
        System.out.println("User from Get addNew:" + user);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("id", user.getId());
        return "add-form";
    }

    @PostMapping(ViewNames.FAIL)
    public String failAdd(@RequestParam Model model) {

        System.out.println("Fail POST");
        User user = (User) model.getAttribute("user");
        model.addAttribute("user", user);
//        if (user == null) {
//            return "/";
//        }
        logger.info("fail method mapping");
        return ViewNames.FAIL;
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
    public String home() {
        return "home";
    }

    @GetMapping("find_by_name_rez")
    public String find(Model model, @RequestParam int id) {
        User user = usersRepository.getUserById(id);
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            System.out.println("find method");
            List<Patient> list = null;
            List<Patient> patients = new ArrayList<>();
            model.addAttribute("nullList", list);
            model.addAttribute("patients", patients);
            model.addAttribute("user", user);
            model.addAttribute("id", id);
            return "find_by_name_rez_form";
        }
    }

    @PostMapping("searchReasult")
    public String findProcess(@RequestParam String name, int id, Model model) {
        System.out.println("In Post find by name");
        User user = (User) model.getAttribute("user");
        model.addAttribute("user", user);
//        if (user == null) {
//            return "/";
//        } else {
        List<Patient> list = new ArrayList<>();
        List<Patient> patients = patientService.getPatientByName(name, user.getTableName());
        if (patients != null) {
            for (Patient patient : patients) {
                System.out.println(patient);
            }
            model.addAttribute("nullList", list);
            model.addAttribute("patients", patients);
        }
        return "find_by_name_rez";
//        }
    }

    @GetMapping("fill_db")
    public void fillDatabase(@RequestParam String tableName) throws IOException {

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
                synchronized (patientService) {
                    patientService.save(Integer.parseInt(arr.get(0)), arr.get(1), arr.get(2), arr.get(3), arr.get(4),
                            replaceDots(arr.get(5)), replaceDots(arr.get(6)), arr.get(7), tableName);
                }
                System.out.println("executed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replaceDots(String date) {
        return date.replace(".", "-");
    }

    @GetMapping("edit")
    public String edit(@RequestParam int id, Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("user", user);
//        if (user == null) {
//            return "/";
//        } else {
        Patient patient = patientService.getPatientById(id, user.getTableName());
        model.addAttribute("patient", patient);
        model.addAttribute("currentDate", getCurrentDate());
        System.out.println("patient = " + patient);
        return "edit";
//        }
    }

    @PostMapping("edit")
    public String processingEdit(@RequestParam
            @NotNull int reportNumber, @NotNull String surname, @NotNull String name,
            @NotNull String fathersName, @NotNull String sex, @NotNull String birthDate,
            @NotNull String deathDate, @NotNull String expert, int id, Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("user", user);
//        if (user == null) {
//            return "/";
//        } else {
        model.addAttribute("massege", "Редагування");
        String tableName = user.getTableName();
        birthDate = Patient.dateToViewMode(birthDate);
        deathDate = Patient.dateToViewMode(deathDate);
        int rez = patientService.updatePatient(reportNumber, name, surname,
                fathersName, sex, birthDate, deathDate, expert, id, tableName);
        if (rez < 1) {
            return ViewNames.FAIL;
        }
        return ViewNames.SUCCESS;
//        }
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

}
