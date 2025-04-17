package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Define custom query methods here if needed
    // For example, you can add methods to find products by category, name, etc.
    // Example: List<Product> findByCategory(String category);

}
