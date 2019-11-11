package com.javamaster.converter;

import com.javamaster.model.CustomUser;
import com.javamaster.model.Role;
import com.javamaster.model.User;
import com.javamaster.repos.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserToUserConverter implements Converter<CustomUser, User> {

    private final RoleRepo roleRepo;

    public CustomUserToUserConverter(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public User convert(CustomUser customUser) {
        User user = new User();

        String id = customUser.getId();
        if (id != null && !id.isEmpty() && !id.equals("0")) {
            user.setId(Long.parseLong(id));
        }
        user.setLogin(customUser.getUsername());
        user.setPassword(customUser.getPassword());

        List<String> roleList = customUser.getRoles();
        List<Role> roles = new ArrayList<>();

        for (String r : roleList) {
            Role role = roleRepo.getRoleByRoleName(r);
            roles.add(role);
        }

        user.setRoles(roles);

        return user;
    }
}
