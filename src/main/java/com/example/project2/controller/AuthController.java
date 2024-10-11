package com.example.project2.controller;

import com.example.project2.model.LoginDto;
import com.example.project2.service.UserDetailsService;
import com.example.project2.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    // Показать страницу входа
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "authentication/login"; // Имя шаблона страницы входа
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDto") LoginDto loginDto, BindingResult result,
                        RedirectAttributes redirectAttributes, HttpSession session) {
        if (result.hasErrors()) {
            return "authentication/login";
        }
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getLogin(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("user", authentication.getPrincipal());

            return "redirect:/users";
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
            return "redirect:/auth/login";
        }
    }
/*
    // Показать страницу регистрации
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "authentication/register"; // Имя шаблона страницы регистрации
    }*/

    /*// Регистрация пользователя
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "authentication/register"; // Вернуть на страницу регистрации в случае ошибок
        }

        try {
            userService.register(userRegistrationDto);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful. You can now log in.");
            return "redirect:/auth/login"; // Перенаправление на страницу входа после успешной регистрации
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/register"; // Вернуться на страницу регистрации с ошибкой
        }
    }*/

    // Выход пользователя
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // Удаляем сессию пользователя
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out successfully.");
        return "redirect:/auth/login"; // Перенаправление на страницу входа
    }
}