package com.avasyspod.angularboot.services;


import com.avasyspod.angularboot.model.*;
import com.avasyspod.angularboot.repository.*;
import com.avasyspod.angularboot.security.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * Created by farshidkhalaj on 9/17/18.
 */

@Service
public class  UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Autowired
    private TabsJpaRepository tabsJpaRepository;
    @Autowired
    private UserRoleJpaRepository userRoleJpaRepository;

    @Autowired
    private RoleTabsJpaRepository roleTabsJpaRepository;

    @Autowired
    private FeatureTabJpaRepository featureTabJpaRepository;

    @Autowired
    private FeaturesJpaRepository featuresJpaRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("HttpServletRequest content is as : " + headerName + ": " + headerValue);
        }

        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.toLowerCase().startsWith("basic"))
        {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);

        }

        System.out.println("LoadUserByUsername in UserDetailsServiceImple Called..........");

        User user = userJpaRepository.findByUsername(username);

        if (user == null) {

            System.out.println("User is null in loadUserByUsername.....");

            throw new UsernameNotFoundException(
                    "Opps! user not found with user-name: " + username);
        }

      /*  return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                getAuthorities(user));*/

      return new CustomUser(user.getUsername(),user.getPassword(),getAuthorities(user),getFeatures(user));
    }

    private Map<String, List<FinalFeatures>> getFeatures(User user) {

        System.out.println("getFeatures method called in UserDetailsServiceImpl..........");

        Map<String, List<FinalFeatures>> tabFeaturesMap = new HashMap<>();

        UserRole userRole = null;
        try {
            userRole = userRoleJpaRepository.findByUserId(user.getId()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Role role = roleJpaRepository.findById(userRole.getRoleId()).get();

        Optional<List<RoleTab>> roleTab = roleTabsJpaRepository.findByRoleId(role.getId());

        if (roleTab.isPresent()) {

            for (RoleTab roleTabs : roleTab.get()) {

                Tabs tabs = tabsJpaRepository.findById(roleTabs.getTabId()).get();

                String tabName = tabs.getName();

                Optional<List<FeatureTab>> featureTab = featureTabJpaRepository.findByTabId(tabs.getId());

                List<FinalFeatures> featuresList = new ArrayList<>();

                if (featureTab.isPresent()) {

                    for (FeatureTab features : featureTab.get()) {

                        Features feature = featuresJpaRepository.findById(features.getFeatureId()).get();

                        FinalFeatures finalFeatures = new FinalFeatures();
                        finalFeatures.setRolename(role.getName());
                        finalFeatures.setFeaturename(feature.getName());
                        finalFeatures.setReadoption(feature.getReadOption());
                        finalFeatures.setReadwriteOption(feature.getReadWriteOption());

                        featuresList.add(finalFeatures);
                    }

                    tabFeaturesMap.put(tabName, featuresList);
                }else{
                    tabFeaturesMap.put(tabName, featuresList);
                }
            }
                return tabFeaturesMap;
        } else {

            return tabFeaturesMap;
        }

    }


    private Collection<GrantedAuthority> getAuthorities(User user) {

        System.out.println("getAuthorities in  UserDetailsServiceImple Called..........");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        UserRole userRole = null;
        try {
            userRole = userRoleJpaRepository.findByUserId(user.getId()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Role role = roleJpaRepository.findById(userRole.getRoleId()).get();

        Optional<List<RoleTab>> roleTab = roleTabsJpaRepository.findByRoleId(role.getId());

        if (roleTab.isPresent()) {
            for (RoleTab roleTabs : roleTab.get()) {

                Tabs tabs = tabsJpaRepository.findById(roleTabs.getTabId()).get();

                grantedAuthorities.add(new SimpleGrantedAuthority(tabs.getName()));

            }

            return grantedAuthorities;
        } else {

            return grantedAuthorities;

        }
    }
}