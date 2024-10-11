package com.example.project2.repository;

import com.example.project2.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<RoleModel, Long> {
    RoleModel findByName(String name);

    boolean existsByName(String name);
}
