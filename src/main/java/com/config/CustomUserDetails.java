package com.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.model.User;

public class CustomUserDetails implements UserDetails{
	private final User user;
	
	public CustomUserDetails(User user) {
        this.user = user;
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
}
