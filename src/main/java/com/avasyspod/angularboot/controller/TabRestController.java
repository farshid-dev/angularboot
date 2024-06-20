package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.TabErrorType;
import com.avasyspod.angularboot.model.Tabs;
import com.avasyspod.angularboot.repository.TabsJpaRepository;
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
@RequestMapping("/api/tab")
public class TabRestController {

    public static final Logger logger = LoggerFactory.getLogger(TabRestController.class);

    private TabsJpaRepository tabsJpaRepository;

    public TabsJpaRepository getTabsJpaRepository() {

        return tabsJpaRepository;
    }

    @Autowired
    public void setTabJpaRepository(TabsJpaRepository tabsJpaRepository)
    {
        this.tabsJpaRepository = tabsJpaRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Tabs>> listAllTabs() {

        logger.info("Fetching all tabs");

        List<Tabs> tabs = tabsJpaRepository.findAll();


        if (tabs.isEmpty()) {
            return new ResponseEntity<List<Tabs>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Tabs>>(tabs, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tabs> getTabById(@PathVariable("id") final Long id) {

        logger.info("Fetching Tab with id {}", id);

        Optional<Tabs> tabsOptional = tabsJpaRepository.findById(id);

        if (tabsOptional.isPresent()) {

            System.out.println("Tab with Selected id is there");

            Tabs tabs = tabsOptional.get();

            System.out.println("Tabname is : "+ tabs.getName());

            return new ResponseEntity<Tabs>(tabs, HttpStatus.OK);

        }

        logger.error("User with id {} not found.", id);

        return new ResponseEntity<Tabs>(new TabErrorType("Tab with id " + id + " not found"),
                HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tabs> createTab(@Valid @RequestBody final Tabs tabs) {

        logger.info("Creating Role : {}", tabs);

        if (tabsJpaRepository.findByName(tabs.getName()) != null) {

            logger.error("Unable to create. A User with name {} already exist", tabs.getName());

            return new ResponseEntity<Tabs>(
                    new TabErrorType(
                            "Unable to create new tab. A Tab with name " + tabs.getName() + " already exist."),
                    HttpStatus.CONFLICT);
        }

        tabs.setName(tabs.getName());

        tabsJpaRepository.save(tabs);

        return new ResponseEntity<Tabs>(tabs, HttpStatus.CREATED);

    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tabs> updateTab(@PathVariable("id") final Long id, @RequestBody Tabs tabs) {

        logger.info("Updating Role with id {}", id);

        Optional<Tabs> tabEntity = tabsJpaRepository.findById(id);

        if (tabEntity.isPresent()) {

            Tabs currentTab = tabEntity.get();

            currentTab.setName(tabs.getName());

            tabsJpaRepository.saveAndFlush(currentTab);

            return new ResponseEntity<Tabs>(currentTab, HttpStatus.OK);

        }

        logger.error("Unable to update. User with id {} not found.", id);
        return new ResponseEntity<Tabs>(
                new TabErrorType("Unable to upate. User with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tabs> deleteTab(@PathVariable("id") final Long id) {

        logger.info("Deleting Tabs with id {}", id);

        Optional<Tabs> tabs = tabsJpaRepository.findById(id);

        if (tabs.isPresent()) {

            tabsJpaRepository.deleteById(id);

            return new ResponseEntity<Tabs>(new TabErrorType("Deleted User with id " + id + "."),
                    HttpStatus.NO_CONTENT);

        }

        logger.error("Unable to delete. User with id {} not found.", id);
        return new ResponseEntity<Tabs>(
                new TabErrorType("Unable to delete. User with id " + id + " not found."), HttpStatus.NOT_FOUND);

    }

}