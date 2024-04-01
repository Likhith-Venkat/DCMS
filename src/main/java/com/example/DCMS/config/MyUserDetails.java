package com.example.DCMS.config;

import com.example.DCMS.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails{
    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        // Ensure password is encoded if applicable
        return user.getPassword();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Convert user roles or permissions to GrantedAuthority objects
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getType()));
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getMc_type()));
        for (String Auth : user.getAccess().split(",")) {  // Assuming roles are comma-separated
            authorities.add(new SimpleGrantedAuthority(Auth));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement logic based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement logic based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement logic based on your requirements
    }

}
