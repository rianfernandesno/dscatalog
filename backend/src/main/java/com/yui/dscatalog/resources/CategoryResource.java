package com.yui.dscatalog.resources;

import com.yui.dscatalog.dto.CategoryDTO;
import com.yui.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){
        List<CategoryDTO> list = categoryService.findAll();
        return  ResponseEntity.ok().body(list);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findByID(@PathVariable Long id){
        CategoryDTO categoryDTO = categoryService.findById(id);

        return  ResponseEntity.ok().body(categoryDTO);
    }
}
