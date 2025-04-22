package com.yui.dscatalog.factory;

import com.yui.dscatalog.dto.ProductDTO;
import com.yui.dscatalog.models.Category;
import com.yui.dscatalog.models.Product;

import java.time.Instant;

public class Factory {

    public  static Product creatProduct(){
        Product product = new Product(1L,"Phone","Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));

        product.getCategories().add(new Category(2L, "Electronics"));
        return  product;
    }

    public static ProductDTO createProductDTO(){
        Product product = creatProduct();
        return  new ProductDTO(product, product.getCategories());
    }

}
