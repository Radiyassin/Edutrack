package org.sid.demo.services;

import org.sid.demo.entities.User;
import org.sid.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    public ResponseEntity<String> validateUserAccess(String email) {
        User byEmail = userRepository.findByEmail(email);

        if (byEmail == null) {
            System.out.println(byEmail);
            // utilisateur non trouvé → accès refusé
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé : utilisateur inconnu");
        }

        // utilisateur trouvé → accès autorisé
        return ResponseEntity.ok("Accès autorisé");
    }

   /* public User getOrCreateUser(String name,String email){
        return userRepository.findByEmail(email).orElseGet(()->{
            User user= new User(name,email,UserRole.STUDENT);
            userRepository.save(user);
            return user;
        });

    }
    public User getAuthenticateUser(String name,String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("l utilsateur n est pas perengesitre"));
    }
   /*public UserRole determineUserFromRole(String email){
        if (email.endsWith("@etu.fh-aachen.de")){
            return UserRole.STUDENT;
        } else if (email.endsWith("@etu.fh-aachen.de")) {
            return UserRole.TEACHER;
        }else {
            throw new RuntimeException("Adresse e-mail inconnue : rôle indéterminé");
        }

    }*/
}
