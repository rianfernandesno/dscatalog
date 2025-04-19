package com.yui.dscatalog.resources;

import com.yui.dscatalog.dto.CategoryDTO;
import com.yui.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(
            @RequestParam(value = "paage", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "ASC") String orderBy
            ){
        PageRequest pageRequest = PageRequest.of(
                page,
                linesPerPage,
                Sort.Direction.valueOf(direction),
                orderBy);

        Page<CategoryDTO> list = categoryService.findAllPaged(pageRequest);

        return  ResponseEntity.ok().body(list);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findByID(@PathVariable Long id){
        CategoryDTO categoryDTO = categoryService.findById(id);

        return  ResponseEntity.ok().body(categoryDTO);
    }

    @PostMapping
    public  ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
        dto = categoryService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
        dto = categoryService.upddate(id, dto);

        return ResponseEntity.ok().body(dto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);

        return  ResponseEntity.noContent().build();
    }

}
