package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.RoleTab;
import com.avasyspod.angularboot.model.RoleTabPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleTabsJpaRepository extends JpaRepository<RoleTab, RoleTabPK> {

    Optional <List<RoleTab>> findByRoleId(long roleId);


}
