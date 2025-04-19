package vn.hoidanit.laptopshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product); // Save a product to the database

    Optional<Product> findById(long id); // Find a product by its ID

    Product deleteById(long id);
}
