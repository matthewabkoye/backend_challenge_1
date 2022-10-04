package com.matt.test.config;

import com.matt.test.model.User;
import com.matt.test.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomeUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User>optUser = userRepository.findByUsername(username);
        User u = optUser.orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        return new CustomUserDetail(u);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User u) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(u.getRole().name());
        Set<SimpleGrantedAuthority>set = new HashSet<>();
        set.add(simpleGrantedAuthority);
        return set;
    }
}
