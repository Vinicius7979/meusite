package com.productstore.InfoStoreAplication.services;

import com.productstore.InfoStoreAplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Integer> {
}
