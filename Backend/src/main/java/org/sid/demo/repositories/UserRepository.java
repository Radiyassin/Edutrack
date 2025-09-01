package org.sid.demo.repositories;

import org.sid.demo.entities.Role;
import org.sid.demo.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String email);//lors de connexion on recoit un email

    boolean existsByEmail(String email);//s il n existe pas on le creer en base avec son role
       //ensuite on peut associer cet utilisateur a ses projet ou bien ses soumissions

    List<User> findByRole(String role);
    List<User> findByRole(Role role , Sort sort);
    List<User> findByProjectId(String projectId, Sort sort);
    List<User> findByProjectIdAndRole(Long projectId, Role role, Sort sort);
}
