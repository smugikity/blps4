package com.football.service.imp;

import com.football.exception.ResourceNotFoundException;
import com.football.model.ERole;
import com.football.model.Role;
import com.football.model.User;
import com.football.repository.RoleRepository;
import com.football.repository.UserRepository;
import com.football.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {
    final
    UserRepository userRepository;
    final
    RoleRepository roleRepository;
    final
    PasswordEncoder encoder;

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addPointsToAllUsers(int pointsToAdd) {
        userRepository.addPointsToAllUsers(pointsToAdd);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User create(String username, String password) throws Exception {
        if(userRepository.existsByUsername(username)) throw new Exception("Username is already taken!");
        User user = new User(username,encoder.encode(password));
        Role userRole = roleRepository.findByName(ERole.USER.getName());
        user.setRoles(Set.of(userRole));
        user = userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user==null) throw new ResourceNotFoundException("User not found in file");
        return user;
    }

    @Override
    public Integer getPointByUsername(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new Exception("User not found in database");
        return user.getPoint();
    }

    @Override
    public User findById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(()->new Exception("User not found in database"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
