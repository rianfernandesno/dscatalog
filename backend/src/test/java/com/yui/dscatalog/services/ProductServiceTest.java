package com.yui.dscatalog.services;

import com.yui.dscatalog.dto.ProductDTO;
import com.yui.dscatalog.models.Product;
import com.yui.dscatalog.factory.Factory;
import com.yui.dscatalog.repositories.ProductRepository;
import com.yui.dscatalog.services.exceptions.DatabaseException;
import com.yui.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private  ProductService  service;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductDTO productDTO;

    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    public void setUp() throws  Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;

        product = Factory.creatProduct();
        page = new PageImpl<>(List.of(product));
        productDTO = Factory.createProductDTO();

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.doThrow(EntityNotFoundException.class).when(repository).getReferenceById(nonExistingId);


        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

    }

    @Test
    public void findAllPageShouldReturnPage(){

        Pageable pageable = PageRequest.of(0,10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);

    }

    @Test
    public  void  findByIdShouldReturnProductDTOWhenIdExisting(){
        ProductDTO result = service.findById(existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExisting(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }
    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            service.update(nonExistingId, productDTO);
        });
    }
    @Test
    public void updateShouldReturnProductDTOWhenIdExisting(){
        ProductDTO dto = new ProductDTO();

        ProductDTO result = service.update(existingId, dto);

        Assertions.assertNotNull(result);
    }


    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () ->{
            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExisting(){
        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExisting(){

        Assertions.assertDoesNotThrow(() -> {
                service.delete(existingId);
        });

        Mockito.verify(repository).deleteById(existingId);
    }
}
