package com.avasyspod.angularboot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "feature_tab", schema = "loginmaster", catalog = "")
@IdClass(FeatureTabPK.class)
public class FeatureTab {

    @Id
    @Column(name = "feature_id")
    private long featureId;

    @Id
    @Column(name = "tab_id")
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
        FeatureTab that = (FeatureTab) o;
        return featureId == that.featureId && tabId == that.tabId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId, tabId);
    }
}
