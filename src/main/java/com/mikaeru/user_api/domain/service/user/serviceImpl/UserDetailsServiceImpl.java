package com.mikaeru.user_api.domain.service.user.serviceImpl;

import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Classe responsável por carregar o usuário com seus dados ao contexto da aplicação.
 * @author Mickael Luiz
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("user not found in database");
        }

        return new org.springframework.security.core.userdetails.User(
                userOptional.get().getUsername(),
                userOptional.get().getPassword(),
                userOptional.get().getRoles()
        );
    }
}
