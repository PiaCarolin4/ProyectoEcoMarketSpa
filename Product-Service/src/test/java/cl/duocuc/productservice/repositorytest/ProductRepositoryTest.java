package cl.duocuc.productservice.repositorytest;

import cl.duocuc.productservice.model.Product;
import cl.duocuc.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testCargaInicialDeUsuarios() {
        List<Product> productos = productRepository.findAll();
        assertEquals(20, productos.size(), "Deben cargarse 20 productos desde data.sql");
    }


}

