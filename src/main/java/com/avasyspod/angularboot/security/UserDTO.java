package com.avasyspod.angularboot.security;

public class UserDTO
{
    private long id;
    private String username;
    private String password;
    private String address;
    private String email;
    private Byte enabled;
    private long userRoleId;
    private String userRoleName;

    public UserDTO()
    {
    }
    public UserDTO(long id,
                   String username,
                   String password,
                   String address,
                   String email,
                   Byte enabled,
                   long userRoleId,
                   String userRoleName)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.address = address;
        this.email = email;
        this.enabled = enabled;
        this.userRoleId = userRoleId;
        this.userRoleName = userRoleName;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getEnabled() {
        return enabled;
    }

    public void setEnabled(Byte enabled) {
        this.enabled = enabled;
    }

    public long getUserRoleId() {

        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {

        this.userRoleId = userRoleId;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }
}
