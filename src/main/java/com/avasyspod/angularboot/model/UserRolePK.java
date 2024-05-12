package com.avasyspod.angularboot.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
public class UserRolePK implements Serializable {
    @Column(name = "user_id")
    @Id
    private long userId;

    @Column(name = "role_id")
    @Id
    private long roleId;

    public UserRolePK() {
    }

    public UserRolePK(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public long getUserId() {

        return userId;
    }

    public void setUserId(long userId) {

        this.userId = userId;
    }

    public long getRoleId() {

        return roleId;
    }

    public void setRoleId(long roleId) {

        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRolePK that = (UserRolePK) o;
        return userId == that.userId && roleId == that.roleId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, roleId);
    }
}
