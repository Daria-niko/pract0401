package com.example.project2.controller;

import com.example.project2.model.UserModel;
import com.example.project2.repository.UserRepository;
import com.example.project2.service.RoleService;
import com.example.project2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // Внедряем бин PasswordEncoder

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPERUSER')")
    public String listUsers(Model model) {
        List<UserModel> users = userService.findAll();
        model.addAttribute("users", users);
        return "listUsers"; // имя HTML-шаблона для отображения списка пользователей
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("roles", roleService.findAll()); // Получение списка всех ролей
        return "createUser"; // Вернуть шаблон для создания пользователя
    }

    @PostMapping
    public String createUser(@ModelAttribute UserModel user) {
        userService.save(user);
        return "redirect:/users"; // перенаправление после создания
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserModel user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "editUser"; // имя HTML-шаблона для редактирования пользователя
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserModel user) {
        user.setId(id);
        userService.save(user);
        return "redirect:/users"; // перенаправление после редактирования
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users"; // перенаправление после удаления
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        model.addAttribute("roles", roleService.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserModel user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Шифрование пароля
        userService.save(user);
        return "redirect:/login";
    }

}
