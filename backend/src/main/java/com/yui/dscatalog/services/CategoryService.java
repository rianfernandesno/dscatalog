package com.yui.dscatalog.services;

import com.yui.dscatalog.dto.CategoryDTO;
import com.yui.dscatalog.models.Category;
import com.yui.dscatalog.repositories.CategoryRepository;
import com.yui.dscatalog.services.exceptions.DatabaseExcpetion;
import com.yui.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category>  list = categoryRepository.findAll(pageRequest);

        return list.map(x -> new CategoryDTO(x));

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return  new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);

        return  new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO upddate(Long id, CategoryDTO dto) {

        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return  new CategoryDTO(entity);
        } catch(EntityNotFoundException e) {
            throw  new ResourceNotFoundException("Id not found " + id);
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!categoryRepository.existsById(id)){
            throw  new ResourceNotFoundException("Resource not found");
        }

        try {
            categoryRepository.deleteById(id);
        }catch (DataIntegrityViolationException e ){
            throw  new DatabaseExcpetion( "Fail");
        }
    }
}
