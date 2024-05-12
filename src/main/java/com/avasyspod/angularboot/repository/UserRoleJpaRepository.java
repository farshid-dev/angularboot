package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.UserRole;
import com.avasyspod.angularboot.model.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleJpaRepository extends JpaRepository<UserRole,UserRolePK> {

    Optional<UserRole> findByUserId(long userId);

}
