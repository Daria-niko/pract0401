package com.example.project2.service;

import com.example.project2.model.RoleModel;
import com.example.project2.model.UserModel;
import com.example.project2.repository.RoleRepository;
import com.example.project2.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Этот метод загружает пользователя по имени для аутентификации
    @PostConstruct
    public void init() {
        if (!userRepository.existsByLogin("DashaLogin")) {
            UserModel user = new UserModel();
            user.setPassword(passwordEncoder.encode("31014891"));
            user.setName("Dasha");
            user.setSurname("Checan");
            user.setPathronymic("Valeriy");
            user.setLogin("DashaLogin");
            user.setRole(roleRepository.findByName("SUPERUSER"));
            userRepository.save(user);
        }
    }

    // Маппинг ролей пользователя на SimpleGrantedAuthority
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(RoleModel role) {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
    }
}
