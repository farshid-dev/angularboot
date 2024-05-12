package com.avasyspod.angularboot.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

public class FeatureTabPK implements Serializable {
    @Column(name = "feature_id")
    @Id

    private long featureId;
    @Column(name = "tab_id")
    @Id

    private long tabId;

    public long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(long featureId) {
        this.featureId = featureId;
    }

    public long getTabId() {
        return tabId;
    }

    public void setTabId(long tabId) {
        this.tabId = tabId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureTabPK that = (FeatureTabPK) o;
        return featureId == that.featureId && tabId == that.tabId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId, tabId);
    }
}
