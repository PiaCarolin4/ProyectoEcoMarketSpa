package cl.duocuc.productservice.servicetest;


import cl.duocuc.productservice.model.Product;
import cl.duocuc.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductTest {

    @Autowired
    private ProductService productService;

    @Test
    void testAddProduct() {
        assertNull(productService.findById("P100"));
        Product newProduct = new Product("P100","colet","color Azul",1300);
        productService.addProduct(newProduct);
        assertNotNull(productService.findById("P100"));
    }

    @Test
    void testFindAll() {
        List<Product> products = productService.findAll();
        assertFalse(products.isEmpty());
    }

    @Test
    void testFindById() {
        Product product = productService.findById("P001");
        assertNotNull(product);
        assertEquals("P001", product.getId());
    }

    @Test
    void testRemoveProduct() {
        boolean deleted = productService.removeProduct("P002");
        assertTrue(deleted);
        Product product = productService.findById("P002");
        assertNull(product);
    }

    @Test
    void testUpdateProduct() {
        Product product = productService.findById("P003");
        assertNotNull(product);
        Product modifiedProduct = new Product("P003", "Botella de acero inoxidable", "Botella térmica reutilizable, 500 ml", 1000);
        productService.updateProduct("P003", modifiedProduct);
        Product newProduct = productService.findById("P003");
        assertEquals(1000, newProduct.getPrecio());

    }


}

