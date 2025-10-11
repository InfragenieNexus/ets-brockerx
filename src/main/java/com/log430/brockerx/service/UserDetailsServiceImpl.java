package com.log430.brockerx.service;

import com.log430.brockerx.entity.User;
import com.log430.brockerx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    
    private UserRepository userRepository;

    @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        // Utiliser User de Spring Security pour renvoyer un UserDetails
        return org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(
                user.getPassword()).build();
    }
}
