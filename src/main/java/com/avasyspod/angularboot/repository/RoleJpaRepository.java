package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by farshidkhalaj on 7/28/19.
 */

@Repository
public interface RoleJpaRepository extends JpaRepository<Role,Long>{

    Optional<Role> findById(Long id);
    Role findByName(String name);


}
