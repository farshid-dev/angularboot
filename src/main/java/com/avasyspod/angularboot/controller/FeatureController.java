package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.FeatureErrorType;
import com.avasyspod.angularboot.Exception.TabErrorType;
import com.avasyspod.angularboot.model.Features;
import com.avasyspod.angularboot.model.Tabs;
import com.avasyspod.angularboot.repository.FeaturesJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/features")
public class FeatureController
{
    public static final Logger logger = LoggerFactory.getLogger(FeatureController.class);

    private FeaturesJpaRepository featuresJpaRepository;

    @Autowired
    public void setFeaturesJpaRepository(FeaturesJpaRepository featuresJpaRepository)
    {
        this.featuresJpaRepository = featuresJpaRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Features>> listAllFeatures()
    {
        logger.info("Fetching all Features");

        List<Features> features = featuresJpaRepository.findAll();

        if (features.isEmpty())
        {
            return new ResponseEntity<List<Features>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Features>>(features, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Features> getFeaturesById(@PathVariable("id") final Long id)
    {
        logger.info("Fetching Feature with id {}", id);

        Optional<Features> features = featuresJpaRepository.findById(id);
        if (features.isPresent())
        {
               System.out.println("Feature with Selected id is there");
            Features feature = features.get();
                System.out.println("Features is : "+ feature.getName());
            return new ResponseEntity<Features>(feature, HttpStatus.OK);
        }
            logger.error("User with id {} not found.", id);

        return new ResponseEntity<Features>(
                new FeatureErrorType("Feature with id " + id + " not found"),
                HttpStatus.NOT_FOUND);
    }


}