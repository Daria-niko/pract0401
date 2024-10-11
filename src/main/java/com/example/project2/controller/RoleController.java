package com.example.project2.controller;

import com.example.project2.model.RoleModel;
import com.example.project2.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listRoles(Model model) {
        List<RoleModel> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "listRoles"; // имя HTML-шаблона
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new RoleModel());
        return "createRoles"; // имя HTML-шаблона для создания роли
    }

    @PostMapping
    public String createRole(@ModelAttribute RoleModel role) {
        roleService.save(role);
        return "redirect:/roles"; // перенаправление после создания
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RoleModel role = roleService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role ID:" + id));
        model.addAttribute("role", role);
        return "editRoles"; // имя HTML-шаблона для редактирования роли
    }

    @PostMapping("/{id}")
    public String updateRole(@PathVariable Long id, @ModelAttribute RoleModel role) {
        role.setId(id);
        roleService.save(role);
        return "redirect:/roles"; // перенаправление после редактирования
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return "redirect:/roles"; // перенаправление после удаления
    }
}

