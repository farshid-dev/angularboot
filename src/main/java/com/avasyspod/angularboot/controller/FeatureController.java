package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.model.Features;
import com.avasyspod.angularboot.repository.FeaturesJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}