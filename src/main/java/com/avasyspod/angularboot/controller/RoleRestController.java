package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.RoleErrorType;
import com.avasyspod.angularboot.model.Role;
import com.avasyspod.angularboot.repository.RoleJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


/**
 * Created by farshidkhalaj on 10/1/20.
 */

@RestController
@RequestMapping("/api/role")
public class RoleRestController {

    public static final Logger logger = LoggerFactory.getLogger(RoleRestController.class);

    private RoleJpaRepository roleJpaRepository;

    public RoleJpaRepository getRoleJpaRepository() {

        return roleJpaRepository;
    }

    @Autowired
    public void setRoleJpaRepository(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Role>> listAllRoles() {

        logger.info("Fetching all roles");

        List<Role> roles = roleJpaRepository.findAll();


        if (roles.isEmpty()) {
            return new ResponseEntity<List<Role>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") final Long id) {

        logger.info("Fetching Role with id {}", id);

        Optional<Role> userOptional = roleJpaRepository.findById(id);

        if (userOptional.isPresent()) {

            System.out.println("User with Selected id is there");

            Role role = userOptional.get();

            System.out.println("Rolename is : "+ role.getName());

            return new ResponseEntity<Role>(role, HttpStatus.OK);

        }

        logger.error("User with id {} not found.", id);

        return new ResponseEntity<Role>(new RoleErrorType("Role with id " + id + " not found"),
                HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> createRole(@Valid @RequestBody final Role role) {

        logger.info("Creating Role : {}", role);

        if (roleJpaRepository.findByName(role.getName()) != null) {

            logger.error("Unable to create. A User with name {} already exist", role.getName());

            return new ResponseEntity<Role>(
                    new RoleErrorType(
                            "Unable to create new user. A User with name " + role.getName() + " already exist."),
                    HttpStatus.CONFLICT);
        }

        role.setName(role.getName());

        roleJpaRepository.save(role);

        return new ResponseEntity<Role>(role, HttpStatus.CREATED);

    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Role> updateRole(@PathVariable("id") final Long id, @RequestBody Role role) {

        logger.info("Updating Role with id {}", id);

        Optional<Role> roleEntity = roleJpaRepository.findById(id);

        if (roleEntity.isPresent()) {

            Role currentRole = roleEntity.get();

            currentRole.setName(role.getName());

            roleJpaRepository.saveAndFlush(currentRole);

            return new ResponseEntity<Role>(currentRole, HttpStatus.OK);

        }

        logger.error("Unable to update. User with id {} not found.", id);
        return new ResponseEntity<Role>(
                new RoleErrorType("Unable to upate. User with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Role> deleteRole(@PathVariable("id") final Long id) {

        logger.info("Deleting User with id {}", id);

        Optional<Role> role = roleJpaRepository.findById(id);

        if (role.isPresent()) {

            roleJpaRepository.deleteById(id);

            return new ResponseEntity<Role>(new RoleErrorType("Deleted User with id " + id + "."),
                    HttpStatus.NO_CONTENT);

        }

        logger.error("Unable to delete. User with id {} not found.", id);
        return new ResponseEntity<Role>(
                new RoleErrorType("Unable to delete. User with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

}
