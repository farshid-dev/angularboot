package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.CustomErrorType;
import com.avasyspod.angularboot.Exception.DTOErrorType;
import com.avasyspod.angularboot.model.Role;
import com.avasyspod.angularboot.model.User;
import com.avasyspod.angularboot.model.UserRole;
import com.avasyspod.angularboot.repository.RoleJpaRepository;
import com.avasyspod.angularboot.repository.UserJpaRepository;
import com.avasyspod.angularboot.repository.UserRoleJpaRepository;
import com.avasyspod.angularboot.security.UserDTO;
import com.avasyspod.angularboot.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {
    public static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserRoleJpaRepository userRoleJpaRepository;
    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRegistrationRestController() {

    }

    public UserRegistrationRestController(UserJpaRepository userJpaRepository,RoleJpaRepository roleJpaRepository,UserRoleJpaRepository userRoleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userRoleJpaRepository = userRoleJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> listAllUsers() {

        logger.info("Fetching all users");

        List<UserDTO> users = userService.getUsersWithRoleNames();

        if (users.isEmpty()) {
            return new ResponseEntity<List<UserDTO>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final Long id) {

        logger.info("Fetching User with id {}", id);

        Optional<User> userEntity = userJpaRepository.findById(id);

        UserRole userRoles = userRoleJpaRepository.findByUserId(id).get();

        Role role = roleJpaRepository.findById(userRoles.getRoleId()).get();

        if (userEntity.isPresent()) {

            System.out.println("User with Selected id is there");

            User user = userEntity.get();

            UserDTO userDTO = new UserDTO();

            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setAddress(user.getAddress());
            userDTO.setEmail(user.getEmail());
            userDTO.setEnabled(user.getEnabled());
            userDTO.setUserRoleId(role.getId());
            userDTO.setUserRoleName(role.getName());

            System.out.println("Username is : "+ userDTO.getUsername());

            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);

        }

        logger.error("User with id {} not found.", id);

        return new ResponseEntity<UserDTO>(new DTOErrorType("User with id " + id + " not found"),
                HttpStatus.NOT_FOUND);

    }

    /**
     * @exception MethodArgumentNotValidException
     *                (validation fails)
     */
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final UserDTO userDTO) {

        logger.info("Creating User : {}", userDTO);

        if (userJpaRepository.findByUsername(userDTO.getUsername()) != null) {

            logger.error("Unable to create. A User with name {} already exist", userDTO.getUsername());

            return new ResponseEntity<UserDTO>(
                    new DTOErrorType(
                            "Unable to create new user. A User with name " + userDTO.getUsername() + " already exist."),
                    HttpStatus.CONFLICT);
        }

        String userPass = userDTO.getPassword();

        String encryptedPassword = passwordEncoder.encode(userPass);

        userDTO.setPassword(encryptedPassword);

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(userDTO.getEnabled());
        user.setPassword(userDTO.getPassword());

        userJpaRepository.save(user);

        long userId = user.getId();

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(userDTO.getUserRoleId());
        userRoleJpaRepository.saveAndFlush(userRole);


        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);

    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") final Long id, @RequestBody UserDTO userDTO) {

        System.out.println(userDTO.getUserRoleId());

        logger.info("Updating User with id {}", id);

        Optional<User> userEntity = userJpaRepository.findById(id);

        if (userEntity.isPresent()) {

            User user = userEntity.get();

            user.setUsername(userDTO.getUsername());

            user.setAddress(userDTO.getAddress());

            user.setEmail(userDTO.getEmail());

            String userPass = userDTO.getPassword();

            String encryptedPassword = passwordEncoder.encode(userPass);

            user.setPassword(encryptedPassword);

            userJpaRepository.saveAndFlush(user);

            UserRole userRoles = userRoleJpaRepository.findByUserId(id).get();

            userRoleJpaRepository.delete(userRoles);

            UserRole userRole = new UserRole(id,userDTO.getUserRoleId());

/*          UserRolePK userRolePK = new UserRolePK();
            userRolePK.setUserId(id);
            userRolePK.setRoleId(userDTO.getUserRoleId());

            UserRole userRole = new UserRole(userRolePK.getUserId(),userRolePK.getRoleId());*/

            try {
                userRoleJpaRepository.saveAndFlush(userRole);
            } catch (Exception e) {
                System.out.println("This is Exception of Update Junction Table" + e.getMessage());
            }

            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);

        }

        logger.error("Unable to update. User with id {} not found.", id);
        return new ResponseEntity<UserDTO>(
                new DTOErrorType("Unable to upate. User with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") final Long id) {

        logger.info("Deleting User with id {}", id);

        Optional<User> user = userJpaRepository.findById(id);

        if (user.isPresent()) {

            UserRole userRoles = userRoleJpaRepository.findByUserId(id).get();

            userRoleJpaRepository.delete(userRoles);
            userJpaRepository.deleteById(id);

            return new ResponseEntity<User>(new CustomErrorType("Deleted User with id " + id + "."),
                    HttpStatus.NO_CONTENT);

        }

        logger.error("Unable to delete. User with id {} not found.", id);
        return new ResponseEntity<User>(
                new CustomErrorType("Unable to delete. User with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

}