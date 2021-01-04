package fr.yncrea.cir3.othello.service;

import fr.yncrea.cir3.othello.domain.User;
import fr.yncrea.cir3.othello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register() {
        User u = new User();
        u.setUsername("clzga");
        u.setEmail("theo@live.fr");
        u.setPassword(passwordEncoder.encode("idinahui"));

        userRepository.save(u);
    }
}
