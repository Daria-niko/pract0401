package com.example.project2.controller;

import com.example.project2.model.ProductModel;
import com.example.project2.model.CategoryModel;
import com.example.project2.model.ManufacturerModel;
import com.example.project2.service.ProductService;
import com.example.project2.service.CategoryService;
import com.example.project2.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ManufacturerService manufacturerService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_SUPERUSER')")
    public String listProducts(Model model) {
        List<ProductModel> products = productService.findAll();
        model.addAttribute("products", products);
        return "listProducts"; // Имя HTML-шаблона для списка продуктов
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_SUPERUSER') or hasAuthority('ROLE_MANAGER')")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductModel());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("manufacturers", manufacturerService.findAll());
        return "createProduct"; // Имя HTML-шаблона для создания продукта
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_SUPERUSER')")
    public String createProduct(@ModelAttribute ProductModel product) {
        productService.save(product);
        return "redirect:/products"; // Перенаправление после создания
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductModel product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("manufacturers", manufacturerService.findAll());
        return "editProduct"; // Имя HTML-шаблона для редактирования продукта
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductModel product) {
        product.setId(id);
        productService.save(product);
        return "redirect:/products"; // Перенаправление после редактирования
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products"; // Перенаправление после удаления
    }
}
