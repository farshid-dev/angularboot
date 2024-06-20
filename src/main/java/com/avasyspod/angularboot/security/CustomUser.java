package com.avasyspod.angularboot.security;

import com.avasyspod.angularboot.model.FinalFeatures;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by farshidkhalaj on 9/25/20.
 */
public class CustomUser extends User {

    private Map<String, List<FinalFeatures>> featuresMap;

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
                      boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities,
                      Map<String, List<FinalFeatures>> featuresMap)
    {
        super(username, password, authorities);
        this.featuresMap = featuresMap;
    }

    public Map<String , List<FinalFeatures>> getFeaturesMap() {

        return featuresMap;
    }

    public void setFeaturesMap(Map<String, List<FinalFeatures>> featuresMap) {

        this.featuresMap = featuresMap;
    }


}
