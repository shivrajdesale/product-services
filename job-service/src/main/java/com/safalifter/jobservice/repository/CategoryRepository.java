package com.shivraj.jobservice.repository;

import com.shivraj.jobservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
