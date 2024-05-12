package com.avasyspod.angularboot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role_tab", schema = "loginmaster", catalog = "")
@IdClass(RoleTabPK.class)
public class RoleTab {

    @Id
    @Column(name = "role_id")
    private long roleId;

    @Id
    @Column(name = "tab_id")
    private long tabId;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
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
        RoleTab roleTab = (RoleTab) o;
        return roleId == roleTab.roleId && tabId == roleTab.tabId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, tabId);
    }
}
