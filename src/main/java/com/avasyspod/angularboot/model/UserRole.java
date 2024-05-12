package com.avasyspod.angularboot.model;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_role", schema = "loginmaster", catalog = "")
@IdClass(UserRolePK.class)
public class UserRole {

    public UserRole() {
    }


    public UserRole(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Id
    @Column(name = "user_id")
    private long userId;


    @Id
    @Column(name = "role_id")
    private long roleId;

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
        UserRole userRole = (UserRole) o;
        return userId == userRole.userId && roleId == userRole.roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
