package com.example.project2.controller;

import com.example.project2.model.OrderModel;
import com.example.project2.service.OrderService;
import com.example.project2.service.ProductService;
import com.example.project2.service.StatusService;
import com.example.project2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final StatusService statusService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, ProductService productService, StatusService statusService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.statusService = statusService;
    }

    @GetMapping
    public String listOrders(Model model) {
        List<OrderModel> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "listOrders";  // Путь к HTML-шаблону
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new OrderModel());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("statuses", statusService.findAll());
        return "createOrder";  // Путь к HTML-шаблону
    }

    @PostMapping
    public String createOrder(@ModelAttribute OrderModel order) {
        orderService.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        OrderModel order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("statuses", statusService.findAll());
        return "editOrder";  // Путь к HTML-шаблону
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id, @ModelAttribute OrderModel order) {
        order.setId(id);
        orderService.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return "redirect:/orders";
    }
}
