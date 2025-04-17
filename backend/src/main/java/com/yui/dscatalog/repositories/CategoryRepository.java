package com.yui.dscatalog.repositories;

import com.yui.dscatalog.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
