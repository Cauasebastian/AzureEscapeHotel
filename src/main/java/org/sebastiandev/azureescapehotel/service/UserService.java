package org.sebastiandev.azureescapehotel.service;

import lombok.RequiredArgsConstructor;
import org.sebastiandev.azureescapehotel.exception.UserAlreadyExistsException;
import org.sebastiandev.azureescapehotel.exception.UsernameNotFoundException;
import org.sebastiandev.azureescapehotel.model.Role;
import org.sebastiandev.azureescapehotel.model.User;
import org.sebastiandev.azureescapehotel.repository.RoleRepository;
import org.sebastiandev.azureescapehotel.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Invalida o cache quando um novo usuário é registrado
    public User registerUser(User user) {
        // Verifica se o usuário já existe no banco de dados pelo e-mail
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }

        // Criptografa a senha do usuário
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Gerencia as roles do usuário
        Set<Role> managedRoles = new HashSet<>();
        user.getRoles().forEach(role -> {
            if (role.getId() != null && roleRepository.existsById(role.getId())) {
                roleRepository.findById(role.getId()).ifPresent(managedRoles::add);
            } else {
                managedRoles.add(role);
            }
        });
        user.setRoles(managedRoles);

        // Atribui a role padrão "ROLE_USER" se o usuário não tiver nenhuma role atribuída
        if (user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found"));
            user.setRoles(Collections.singleton(userRole));
        }

        // Salva o usuário no banco de dados
        return userRepository.save(user);
    }

    @Override
    @Cacheable("users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    @CacheEvict(value = "users", allEntries = true) // Invalida o cache quando um usuário é deletado
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}