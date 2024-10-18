package ru.luckyskeet.usermanagment.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.luckyskeet.usermanagment.model.User;
import ru.luckyskeet.usermanagment.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = repository.findByName(userName);
        return user.map(UserDetailsImpl::new).orElseThrow();
    }
}
