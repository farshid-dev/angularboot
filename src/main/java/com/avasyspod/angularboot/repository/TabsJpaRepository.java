package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.Tabs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by farshidkhalaj on 3/4/21.
 */
public interface TabsJpaRepository extends JpaRepository<Tabs,Long> {

    Tabs findByName(String name);
}
