package fr.n7.spring_boot_api.payload.response;

import java.util.List;

import fr.n7.spring_boot_api.model.ERole;

public class RoleUpdateResponse {
    private Long userId;
    private String username;
    private List<ERole> roles;

    public RoleUpdateResponse(Long userId, String username, List<ERole> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ERole> getRoles() {
        return roles;
    }

    public void setRoles(List<ERole> roles) {
        this.roles = roles;
    }
}