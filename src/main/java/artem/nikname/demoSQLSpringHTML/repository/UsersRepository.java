/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artem.nikname.demoSQLSpringHTML.repository;

import artem.nikname.demoSQLSpringHTML.model.Patient;
import artem.nikname.demoSQLSpringHTML.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Master
 */
public interface UsersRepository extends JpaRepository<User, Integer>{
    
    String TABLE = "User";
    
    @Query(value = "SELECT u FROM " + TABLE + " u WHERE u.login = ?1 AND u.password = ?2")
    public User getUserByLoginAndPassword(String login,String password);
    
    
    @Query(value = "SELECT u FROM " + TABLE + " u WHERE u.id = ?1")
    public User getUserById(int id);
    
     
    
    
}
