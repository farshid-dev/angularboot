package com.avasyspod.angularboot.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

public class RoleTabPK implements Serializable {
    @Column(name = "role_id")
    @Id
    private long roleId;
    @Column(name = "tab_id")
    @Id
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
        RoleTabPK roleTabPK = (RoleTabPK) o;
        return roleId == roleTabPK.roleId && tabId == roleTabPK.tabId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, tabId);
    }
}
