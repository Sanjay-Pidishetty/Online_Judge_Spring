package com.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.model.User;

public class CustomUserDetails implements UserDetails{
	private final User user;
	private List<GrantedAuthority> authorities;
	
	public CustomUserDetails(User user) {
        this.user = user;
        this.authorities = new ArrayList<>();
        
        // Assuming that the roles are stored as a comma-separated string
        // For example: "ROLE_USER,ROLE_ADMIN"
        if (user.getRole() != null && !user.getRole().isEmpty()) {
                authorities.add(new SimpleGrantedAuthority(user.getRole().trim()));
        }
    }
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(
		        new SimpleGrantedAuthority("ROLE_Admin"),
		        new SimpleGrantedAuthority("ROLE_User")
		    );
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Return stored hashed password
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public List<String> getRoles() {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
