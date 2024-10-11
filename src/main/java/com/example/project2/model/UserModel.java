package com.example.project2.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")  // Renamed to "users" for consistency
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(max = 25, message = "Максимальная длинна = 25 символов")
    private String name;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(max = 25, message = "Максимальная длинна = 25 символов")
    private String surname;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(max = 25, message = "Максимальная длинна = 25 символов")
    private String pathronymic;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, max = 25, message = "Логин должен состоять из 3 - 25 символов")
    private String login;

    @Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",
            message = "Пароль должен содержать минимум одну цифру, одну строчную букву, одну заглавную букву и один специальный символ")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")  // Explicitly set referencedColumnName
    private RoleModel role;

    private boolean deleted;  // Removed @NotBlank
}
