/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.security;

import org.springframework.stereotype.Component;

/**
 *
 * @author Master
 */
@Component
public class SecurityCheck {

    public boolean checkForInjection(String login, String password) {
        return check(login) & check(password);
    }

    private boolean check(String testString) {
        String arrInjection[] = {"drop", "or", "insert", "where", "select",
            "and", "*", ";", "-", "--", "=", "'", "$", "(", ")"};
        testString = testString.toLowerCase();
        for (String string : arrInjection) {
            if (testString.contains(string)) {
                System.out.println("detected injection - " + string);
                return false;
            }
        }
        return true;
    }

}
