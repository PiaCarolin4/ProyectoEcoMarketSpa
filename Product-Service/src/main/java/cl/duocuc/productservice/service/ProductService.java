package cl.duocuc.productservice.service;

import cl.duocuc.productservice.model.Product;
import cl.duocuc.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository ProductRepository;

    public ProductService(ProductRepository productRepository) {
        this.ProductRepository = productRepository;
    }

    public List<Product> findAll() {
        return ProductRepository.findAll();
    }

    public Product findById(String id) {
        return ProductRepository.findById(id).orElse(null);
    }

    public boolean addProduct(Product product) {
        if (ProductRepository.existsById(product.getId())) {
            return false;
        }
        ProductRepository.save(product);
        return true;
    }

    public boolean updateProduct(String id, Product product) {
        if (ProductRepository.existsById(id)) {
            ProductRepository.save(product);
            return true;
        }
        return false;
    }

    public boolean removeProduct(String id) {
        if (ProductRepository.existsById(id)) {
            ProductRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
