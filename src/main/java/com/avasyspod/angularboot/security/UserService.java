package com.avasyspod.angularboot.security;

import com.avasyspod.angularboot.model.Role;
import com.avasyspod.angularboot.model.User;
import com.avasyspod.angularboot.model.UserRole;
import com.avasyspod.angularboot.repository.RoleJpaRepository;
import com.avasyspod.angularboot.repository.UserJpaRepository;
import com.avasyspod.angularboot.repository.UserRoleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserRoleJpaRepository userRoleJpaRepository;
    @Autowired
    private RoleJpaRepository roleJpaRepository;

    public UserService() {
    }

    public UserService(UserJpaRepository userJpaRepository) {

        this.userJpaRepository = userJpaRepository;
    }

    public List<UserDTO> getUsersWithRoleNames() {

        List<UserDTO> userDTOs = new ArrayList<>();
        List<UserRole> userRoles = userRoleJpaRepository.findAll();


        for (UserRole userRole:userRoles) {

            User user = userJpaRepository.findById(userRole.getUserId()).get();
            Role role = roleJpaRepository.findById(userRole.getRoleId()).get();

            System.out.println("Role Id : " + role.getId());

            UserDTO userDTO = new UserDTO();

            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setAddress(user.getAddress());
            userDTO.setEmail(user.getEmail());
            userDTO.setEnabled(user.getEnabled());
            userDTO.setUserRoleId(role.getId());
            userDTO.setUserRoleName(role.getName());

            userDTOs.add(userDTO);

        }

        return userDTOs;
    }

}
