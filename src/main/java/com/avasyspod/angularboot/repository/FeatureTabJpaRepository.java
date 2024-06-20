package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.FeatureTab;
import com.avasyspod.angularboot.model.FeatureTabPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeatureTabJpaRepository extends JpaRepository<FeatureTab, FeatureTabPK>
{
    Optional<List<FeatureTab>> findByTabId(Long id);
}
