package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by farshidkhalaj on 9/19/18.
 */

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);

    User findByUsername(String username);
}
