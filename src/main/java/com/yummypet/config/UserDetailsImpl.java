package com.yummypet.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yummypet.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @JsonIgnore
    private final User user;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getRole().getName();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override 
    @JsonIgnore
    public String getPassword() { 
        return user.getPasswordHash(); 
    }

    @Override 
    public String getUsername() { 
        return user.getUsername(); 
    }

    @Override 
    @JsonIgnore
    public boolean isAccountNonExpired() { 
        return true; 
    }

    @Override 
    @JsonIgnore
    public boolean isAccountNonLocked() { 
        return true; 
    }

    @Override 
    @JsonIgnore
    public boolean isCredentialsNonExpired() { 
        return true; 
    }

    @Override 
    @JsonIgnore
    public boolean isEnabled() { 
        return user.getIsActive(); 
    }

    public User getUser() { return user; }
}
