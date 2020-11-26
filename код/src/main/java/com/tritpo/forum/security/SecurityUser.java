package com.tritpo.forum.security;

import com.tritpo.forum.enums.Status;
import com.tritpo.forum.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityUser(String username, String password, List<GrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    public SecurityUser(User user) {
        this.username = user.getName();
        this.password = user.getPassword();
        this.authorities = new ArrayList<GrantedAuthority>();
        this.authorities.add(user.getRole().iterator().next());
        this.isActive = user.getStatus().equals(Status.ACTIVE);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(),
                true,
                true,
                true,
                true,
                user.getRole()
        );
    }

}
