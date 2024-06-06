package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.FeatureTabErrorType;
import com.avasyspod.angularboot.model.FeatureTab;
import com.avasyspod.angularboot.model.Features;
import com.avasyspod.angularboot.model.Tabs;
import com.avasyspod.angularboot.repository.FeatureTabJpaRepository;
import com.avasyspod.angularboot.repository.FeaturesJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tabfeatures")
public class TabFeatureController
{
    public static final Logger logger = LoggerFactory.getLogger(FeatureController.class);
    private FeatureTabJpaRepository featureTabJpaRepository;
    private FeaturesJpaRepository featuresJpaRepository;

    public TabFeatureController(FeatureTabJpaRepository featureTabJpaRepository,
                                FeaturesJpaRepository featuresJpaRepository)
    {
        this.featureTabJpaRepository = featureTabJpaRepository;
        this.featuresJpaRepository = featuresJpaRepository;
    }

    public FeatureTabJpaRepository getFeatureTabJpaRepository()
    {
        return featureTabJpaRepository;
    }

    @Autowired
    public void setFeatureTabJpaRepository(FeatureTabJpaRepository featureTabJpaRepository)
    {
        this.featureTabJpaRepository = featureTabJpaRepository;
    }

    public FeaturesJpaRepository getFeaturesJpaRepository()
    {
        return featuresJpaRepository;
    }

    @Autowired
    public void setFeaturesJpaRepository(FeaturesJpaRepository featuresJpaRepository)
    {
        this.featuresJpaRepository = featuresJpaRepository;
    }


    @GetMapping("/")
    public ResponseEntity<List<FeatureTab>> listAllFeatureTab()
    {
        logger.info("Fetching all FeaturesTab");
        List<FeatureTab> featureTabs = featureTabJpaRepository.findAll();

        if (featureTabs.isEmpty())
        {
            return new ResponseEntity<List<FeatureTab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<FeatureTab>>(featureTabs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getFeature(@PathVariable("id") final Long id)
    {
        logger.info("Fetching Feature with id {}", id);

        Optional<List<FeatureTab>> featureTabs = featureTabJpaRepository.findByTabId(id);

        List<Features> assignedFeatures = new ArrayList<>();

        if (featureTabs.isPresent())
        {
            for (FeatureTab featureTab : featureTabs.get())
            {
                Optional<Features> feature = featuresJpaRepository.findById(featureTab.getFeatureId());
                Features features = feature.get();
                assignedFeatures.add(features);
                System.out.println("Assigned Features : " + assignedFeatures);
            }
                List<Features> allFeature = featuresJpaRepository.findAll();
                List<Features> availableFeatures = new ArrayList<>();
            for (Features feature : allFeature)
            {
                if (!assignedFeatures.contains(feature))
                {
                    availableFeatures.add(feature);
                }
            }
            Map<String, List<Features>> featurssMap = new HashMap<>();

            featurssMap.put("availableFeatures", availableFeatures);
            featurssMap.put("assignedFeatures", assignedFeatures);

            return new ResponseEntity<Map>(featurssMap, HttpStatus.OK);
        }
            logger.error("Features with id {} not found.", id);
            return new ResponseEntity<>(
                    new FeatureTabErrorType("Featurs with id " + id + " not found").toMap(),
                    HttpStatus.NOT_FOUND);

    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureTab> updateFeatureTab(@PathVariable("id") final Long id, @RequestBody List<Features> features)
    {
        logger.info("Updating FeatureTab with id {}", id);

        Optional<List<FeatureTab>> featureTabs = featureTabJpaRepository.findByTabId(id);

        if (featureTabs.isPresent())
        {
            for(FeatureTab featureTab:featureTabs.get())
                featureTabJpaRepository.delete(featureTab);
        }

        FeatureTab featureTab = new FeatureTab();

        for (Features features1:features)
        {
            System.out.println(features1.getName());
            featureTab.setTabId(features1.getId());
            featureTab.setFeatureId(id);
            featureTabJpaRepository.saveAndFlush(featureTab);
        }
        return new ResponseEntity<>((FeatureTab)
                new FeatureTabErrorType("Feature with id: "+ id + "not found").toMap(),
                HttpStatus.NOT_FOUND);
    }
}


