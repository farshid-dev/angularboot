package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.FeatureErrorType;
import com.avasyspod.angularboot.model.Features;
import com.avasyspod.angularboot.repository.FeaturesJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Features> createFeatures(@Valid @RequestBody final Features features)
    {
        logger.info("Creating Features : {}", features);

        if (featuresJpaRepository.findByName(features.getName()) != null)
        {
            logger.error("Unable to create. A Feature with name {} already exist", features.getName());

            return new ResponseEntity<Features>(
                    new FeatureErrorType(
                            "Unable to create new feature. A Feature with name " +
                                    features.getName() +
                                    " already exist."),
                    HttpStatus.CONFLICT);
        }

        features.setName(features.getName());
        featuresJpaRepository.save(features);
        return new ResponseEntity<Features>(features, HttpStatus.CREATED);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Features> UpdateFeature(@PathVariable("id") final Long id, @RequestBody Features features)
    {
        logger.info("Updating Feature with id {}", id);

        Optional<Features> features1 = featuresJpaRepository.findById(id);

        if (features1.isPresent())
        {
            Features currentFeature = features1.get();
            currentFeature.setName(features.getName());
            currentFeature.setReadOption(features.getReadOption());
            currentFeature.setReadWriteOption(features.getReadWriteOption());

            featuresJpaRepository.saveAndFlush(currentFeature);
            return new ResponseEntity<Features>(currentFeature, HttpStatus.OK);
        }
        logger.error("Unable to update. User with id {} not found.", id);
        return new ResponseEntity<Features>(
                new FeatureErrorType("Unable to update. Feature with id " + id + " not found."),
                HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Features> deleteFeature(@PathVariable("id") final Long id)
    {
        logger.info("Deleting Feature with id {}", id);

        Optional<Features> features = featuresJpaRepository.findById(id);

        if (features.isPresent())
        {
            featuresJpaRepository.deleteById(id);
            return new ResponseEntity<Features>(
                    new FeatureErrorType("Deleted Feature with id " + id + "."),
                    HttpStatus.NO_CONTENT);
        }
        logger.error("Unable to delete. User with id {} not found.", id);
        return new ResponseEntity<Features>(
                new FeatureErrorType("Unable to delete. Feature with id " + id + " not found."),
                HttpStatus.NOT_FOUND);
    }
}