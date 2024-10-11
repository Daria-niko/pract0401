    package com.example.project2.controller;

    import com.example.project2.model.CategoryModel;
    import com.example.project2.service.CategoryService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/categories")
    public class CategoryController {

        private final CategoryService categoryService;

        @Autowired
        public CategoryController(CategoryService categoryService) {
            this.categoryService = categoryService;
        }

        @GetMapping
        public String listCategories(@RequestParam(value = "sort", required = false, defaultValue = "asc") String sortDirection, Model model) {
            List<CategoryModel> categories = categoryService.findAllSorted(sortDirection);
            model.addAttribute("categories", categories);
            model.addAttribute("currentSort", sortDirection);
            return "listCategories";  // Путь к HTML-шаблону
        }

        @GetMapping("/search")
        public String searchCategories(@RequestParam("name") String name,
                                       @RequestParam(value = "sort", required = false, defaultValue = "asc") String sortDirection,
                                       Model model) {
            List<CategoryModel> categories = categoryService.searchByName(name, sortDirection);
            model.addAttribute("categories", categories);
            model.addAttribute("currentSort", sortDirection);
            return "listCategories";
        }

        @GetMapping("/categories/filter")
        public String filterCategories(
                @RequestParam(value = "deleted", required = false, defaultValue = "false") boolean showDeleted,
                Model model) {

            List<CategoryModel> categories;

            if (showDeleted) {
                categories = categoryService.findAllCategories();  // Показать все категории, включая удаленные
            } else {
                categories = categoryService.findActiveCategories();  // Показать только активные категории
            }

            model.addAttribute("categories", categories);
            model.addAttribute("deleted", !showDeleted);  // Переключаем состояние для кнопки
            return "listCategories"; // Убедитесь, что возвращаете правильное имя шаблона
        }

        @GetMapping("/create")
        public String showCreateForm(Model model) {
            model.addAttribute("category", new CategoryModel());
            return "createCategory";  // Путь к HTML-шаблону
        }

        @PostMapping
        public String createCategory(@ModelAttribute CategoryModel category) {
            categoryService.save(category);
            return "redirect:/categories";
        }

        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable Long id, Model model) {
            CategoryModel category = categoryService.findById(id);
            model.addAttribute("category", category);
            return "editCategory";  // Путь к HTML-шаблону
        }

        @PostMapping("/edit/{id}")
        public String updateCategory(@PathVariable Long id, @ModelAttribute CategoryModel category) {
            category.setId(id);
            categoryService.save(category);
            return "redirect:/categories";
        }

        // Логическое удаление
        @GetMapping("/soft-delete/{id}")
        public String softDeleteCategory(@PathVariable Long id) {
            categoryService.softDeleteById(id);  // Логическое удаление
            return "redirect:/categories";
        }

        // Восстановление
        @GetMapping("/restore/{id}")
        public String restoreCategory(@PathVariable Long id) {
            categoryService.restoreById(id);  // Восстановление логически удаленной категории
            return "redirect:/categories";
        }

        // Физическое удаление
        @GetMapping("/hard-delete/{id}")
        public String hardDeleteCategory(@PathVariable Long id) {
            categoryService.hardDeleteById(id);  // Физическое удаление
            return "redirect:/categories";
        }
    }
