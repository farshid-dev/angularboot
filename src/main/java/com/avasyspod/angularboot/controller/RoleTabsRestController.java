package com.avasyspod.angularboot.controller;

import com.avasyspod.angularboot.Exception.RoleTabsErrorType;
import com.avasyspod.angularboot.model.RoleTab;
import com.avasyspod.angularboot.model.Tabs;
import com.avasyspod.angularboot.repository.RoleTabsJpaRepository;
import com.avasyspod.angularboot.repository.TabsJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/roletabs")
public class RoleTabsRestController
{
    public static final Logger logger = LoggerFactory.getLogger(TabRestController.class);
    private RoleTabsJpaRepository roleTabsJpaRepository;
    private TabsJpaRepository tabsJpaRepository;

    public RoleTabsRestController(RoleTabsJpaRepository roleTabsJpaRepository, TabsJpaRepository tabsJpaRepository)
    {
        this.roleTabsJpaRepository = roleTabsJpaRepository;
        this.tabsJpaRepository = tabsJpaRepository;
    }
    public TabsJpaRepository getTabsJpaRepository() {
        return tabsJpaRepository;
    }

    public void setTabsJpaRepository(TabsJpaRepository tabsJpaRepository)
    {
        this.tabsJpaRepository = tabsJpaRepository;
    }

    public RoleTabsJpaRepository getRoleTabsJpaRepository()
    {
        return roleTabsJpaRepository;
    }

    @Autowired
    public void setRoleTabsJpaRepository(RoleTabsJpaRepository roleTabsJpaRepository)
    {
        this.roleTabsJpaRepository = roleTabsJpaRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<RoleTab>> listAllRoleTabs()
    {
        logger.info("Fetching all roleTabs");

        List<RoleTab> roleTabs = roleTabsJpaRepository.findAll();

        if (roleTabs.isEmpty())
        {
            return new ResponseEntity<List<RoleTab>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<RoleTab>>(roleTabs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getTabs(@PathVariable("id") final Long id)
    {
        logger.info("Fetching Tab with id {}", id);

        Optional<List<RoleTab>> roleTabsOptional = roleTabsJpaRepository.findByRoleId(id);

        List<Tabs> assignedTab = new ArrayList<>();

        if (roleTabsOptional.isPresent()) {

            for (RoleTab roleTab:roleTabsOptional.get()) {

                  Optional<Tabs> tabs = tabsJpaRepository.findById(roleTab.getTabId());
                  Tabs tab = tabs.get();
                  assignedTab.add(tab);
                  System.out.println("Assigned Tabs : " + assignedTab);

            }

            List<Tabs> allTabs = tabsJpaRepository.findAll();

            List<Tabs> availableTab = new ArrayList<>();

            for (Tabs tab : allTabs) {

                if (!assignedTab.contains(tab)) {

                    availableTab.add(tab);
                }
            }

            Map<String,List<Tabs>> tabsMap = new HashMap<>();

            tabsMap.put("availableTab", availableTab);
            tabsMap.put("assignedTab", assignedTab);

            return new ResponseEntity<Map>(tabsMap, HttpStatus.OK);

        }

            logger.error("Tabs with id {} not found.", id);

            return new ResponseEntity<>(new RoleTabsErrorType("Tabs with id " + id + " not found").toMap(),
                    HttpStatus.NOT_FOUND);

    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleTab> updateRoleTab(@PathVariable("id") final Long id, @RequestBody List<Tabs> tabs) {

        logger.info("Updating RoleTabs with id {}", id);

        Optional<List<RoleTab>> roleTabsEntity = roleTabsJpaRepository.findByRoleId(id);

        if (roleTabsEntity.isPresent()) {

            for (RoleTab roleTab:roleTabsEntity.get()) {

                roleTabsJpaRepository.delete(roleTab);

            }

            RoleTab roleTab = new RoleTab();

            for (Tabs tab:tabs) {

                System.out.println(tab.getName());
                roleTab.setTabId(tab.getId());
                roleTab.setRoleId(id);
                roleTabsJpaRepository.saveAndFlush(roleTab);
            }

            //roleTabsJpaRepository.saveAndFlush(roleTab);

            return new ResponseEntity<RoleTab>(roleTab, HttpStatus.OK);

        }

        logger.error("Unable to update. User with id {} not found.", id);

        return new ResponseEntity<RoleTab>((RoleTab) new RoleTabsErrorType("Tabs with id " + id + " not found").toMap(),
                HttpStatus.NOT_FOUND);
    }

}
