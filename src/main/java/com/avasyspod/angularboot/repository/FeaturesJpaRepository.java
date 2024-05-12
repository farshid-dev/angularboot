package com.avasyspod.angularboot.repository;

import com.avasyspod.angularboot.model.Features;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by farshidkhalaj on 8/13/20.
 */

@Repository
public interface FeaturesJpaRepository extends JpaRepository<Features,Long>{


}
