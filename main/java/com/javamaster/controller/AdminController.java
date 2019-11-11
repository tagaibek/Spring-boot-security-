package com.javamaster.controller;

import com.javamaster.repos.RoleRepo;
import com.javamaster.repos.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.javamaster.model.Role;
import com.javamaster.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


    @RequestMapping(value = {"", "/users"}, method = RequestMethod.GET)
    public String listUsers(Model model ) {
        model.addAttribute("roleList", roleRepo.findAll());
        return "browse2";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleRepo.findAll());

        return "create";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute User user, @RequestParam List<String> searchValues) {
        for (String roleStr : searchValues) {
            Role role = roleRepo.getRoleByRoleName(roleStr);
            user.getRoles().add(role);
        }
        userRepo.save(user);

        return "redirect:/admin/users";
    }

    @RequestMapping("/users/remove/{id}")
    public String removeUser(@PathVariable("id") long id) {
        userRepo.deleteById(id);

        return "redirect:/admin/users";
    }

    @RequestMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            redirectAttributes.addFlashAttribute("error_message", "The user you are looking for doesn't exist");
            return "redirect:/admin/users";
        }
        model.addAttribute("roleList", roleRepo.findAll());
        model.addAttribute("user", optionalUser.get());

        redirectAttributes.addFlashAttribute("success_message", "The changes has been successfully saved.");

        return "edit";
    }

    @PostMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") long id, @ModelAttribute User user, @RequestParam List<String> searchValues) {
        user.setId(id);
        for (String roleStr : searchValues) {
            Role role = roleRepo.getRoleByRoleName(roleStr);
            user.getRoles().add(role);
        }
        userRepo.save(user);

        return "redirect:/admin/users";
    }
}

