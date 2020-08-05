/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.controller;

import artem.nikname.demoSQLSpringHTML.model.IdClass;
import artem.nikname.demoSQLSpringHTML.model.Patient;
import artem.nikname.demoSQLSpringHTML.model.SearchClass;
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
import org.springframework.beans.factory.annotation.Required;
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

    public static String getCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(currentDate);
    }

    public static int getCurrentYear() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return Integer.parseInt(sdf.format(currentDate));
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

    @ModelAttribute("searchClass")
    public SearchClass getSearchClass() {
        return new SearchClass();
    }

    @PostMapping("add")
    public String processAdd(@ModelAttribute("idClass") IdClass idClass,
            @RequestParam int reportNumber, String surname, String name, String fathersName,
            String sex, String yearOfBirth, String examDate, String expert, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null) {
            return "redirect:/login";
        } else {

            LocalDate date = LocalDate.parse(examDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            logger.info("add post method mapping");
            model.addAttribute("massege", "Додавання в базу");
            model.addAttribute("id", idClass.getId());
            synchronized (patientService) {
                Patient patient = null;
                if ((patient = patientService.save(reportNumber, surname, name,
                        fathersName, sex, yearOfBirth, date, expert,
                        user.getTableName())) != null) {
                    model.addAttribute("patient", patient);
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
            model.addAttribute("currentYear", getCurrentYear());
            return "add-form";
        }
    }

    @GetMapping("success")
    public String success(@ModelAttribute("IdClass") IdClass idClass,
            @RequestParam int patientId, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            Patient patient = patientService.getPatientById(patientId, user.getTableName());
            model.addAttribute("user", user);
            model.addAttribute("massege", "Додавання в базу");
            model.addAttribute("id", user.getId());
            model.addAttribute("patient", patient);
            return "success-form";
        }
    }

    @GetMapping("home")
    public String home(@ModelAttribute("idClass") IdClass idClass, Model model) {
        model.addAttribute("id", idClass.getId());
        return "home";
    }

    @GetMapping("find_by_name_rez")
    public String find(@ModelAttribute("idClass") IdClass idClass,
            @RequestParam(required = false) @ModelAttribute("searchClass") SearchClass searchClass,
            Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
//            String name = searchName;
//            if (name != null) {
//                model.addAttribute("searchName", name);
//            }
            List<Patient> patients = new ArrayList<>();
            model.addAttribute("searchClass", searchClass);
            model.addAttribute("messageNum", 0);
            model.addAttribute("patients", patients);
            model.addAttribute("currentYear", getCurrentYear());
            model.addAttribute("user", user);
            model.addAttribute("id", idClass.getId());
            return "find_by_name_rez_form";
        }
    }

    @PostMapping("searchReasult")
    public String findProcess(@ModelAttribute("idClass") IdClass idClass,
            @ModelAttribute("searchClass") SearchClass searchClass,
            Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            List<Patient> list = new ArrayList<>();
            List<Patient> patients = patientService.getPatientByNameSurnameYear(searchClass.getSearchName(),
                    searchClass.getSearchSurname(), searchClass.getYearFrom(),
                    searchClass.getYearTo(), user.getTableName());

            model.addAttribute("id", user.getId());
            model.addAttribute("searchClass", searchClass);
            model.addAttribute("currentYear", getCurrentYear());
            model.addAttribute("messageNum", 1);
            model.addAttribute("patients", patients);
            return "find_by_name_rez_form";
        }
    }

    @GetMapping("fill_db")
    public String fillDatabase(@RequestParam String tableName, int id, Model model) throws IOException {

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
//                    patientService.save(Integer.parseInt(arr.get(0)), arr.get(1), arr.get(2), arr.get(3), arr.get(4),
//                            Patient.dateForInputDate(replaceDots(arr.get(5))), Patient.dateForInputDate(replaceDots(arr.get(6))), arr.get(7), tableName);
                }
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
    public String edit(@ModelAttribute("idClass") IdClass idClass, @RequestParam int patientId, Model model) {
        User user = usersRepository.getUserById(idClass.getId());
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            Patient patient = patientService.getPatientById(patientId, user.getTableName());
            model.addAttribute("id", user.getId());
            model.addAttribute("patient", patient);
            model.addAttribute("currentDate", getCurrentDate());
            model.addAttribute("currentYear", getCurrentYear());
            return "edit-form";
        }
    }

    @PostMapping("edit")
    public String processingEdit(@RequestParam int reportNumber, String surname, String name,
            String fathersName, String sex, String yearOfBirth,
            String examDate, String expert, int patientId, int id, Model model) {
        User user = usersRepository.getUserById(id);
        if (user == null || !users.contains(user)) {
            return "redirect:/showLoginForm";
        } else {
            
            Patient patient = patientService.getPatientById(patientId, user.getTableName());
            System.out.println(" \u001b31m EDIT patient = " + patient);
            model.addAttribute("id", id);
            model.addAttribute("massege", "Редагування");
            model.addAttribute("patient",patient);
            String tableName = user.getTableName();
            int rez = patientService.updatePatient(reportNumber, name, surname,
                    fathersName, sex, yearOfBirth, examDate, expert, patientId, tableName);
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
        List<Patient> patients = patientService.findAll(user.getTableName());
        model.addAttribute("patients", patients);
        return "showUsers";
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

    @GetMapping("delete")
    public String delete(@ModelAttribute("searchClass") SearchClass searchClass,
            @RequestParam int id, String searchName, int patientId,
            boolean yesAnswer, Model model) {
        User user = usersRepository.getUserById(id);
        String tableName = user.getTableName();
        if (yesAnswer) {
            patientService.deletePatient(patientId, tableName);
        }
        System.out.println(searchClass);
        model.addAttribute("messageNum", 1);
        model.addAttribute("currentYear", getCurrentYear());
        model.addAttribute("patients", patientService.getPatientByNameSurnameYear(
                searchClass.getSearchName(), searchClass.getSearchSurname(),
                searchClass.getYearFrom(), searchClass.getYearTo(),
                user.getTableName()));
        model.addAttribute("patientId", patientId);
        model.addAttribute("id", id);

        return "find_by_name_rez_form";
    }

}
