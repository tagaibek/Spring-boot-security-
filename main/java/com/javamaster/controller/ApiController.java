package com.javamaster.controller;

import com.javamaster.converter.CustomUserToUserConverter;
import com.javamaster.model.CustomUser;
import com.javamaster.model.User;
import com.javamaster.repos.RoleRepo;
import com.javamaster.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;


    @GetMapping(path = "/user/{id}")
    public ResponseEntity<User> user(@PathVariable Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(optionalUser.get());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> create(CustomUser customUser) {
        CustomUserToUserConverter customUserToUserConverter = new CustomUserToUserConverter(roleRepo);
        User user = customUserToUserConverter.convert(customUser);
        User userNew = userRepo.save(user);
        if (userNew == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userNew);
        }
    }

    @PutMapping(path = "/user/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<User> update(CustomUser customUser) {
        CustomUserToUserConverter customUserToUserConverter = new CustomUserToUserConverter(roleRepo);
        User user = customUserToUserConverter.convert(customUser);
        User userUpd = userRepo.save(user);
        if (userUpd == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userUpd);
        }
    }

    @DeleteMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            userRepo.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "User successful deleted."));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
