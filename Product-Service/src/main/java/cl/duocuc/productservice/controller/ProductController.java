package cl.duocuc.productservice.controller;

import cl.duocuc.productservice.controller.response.MessageResponse;
import cl.duocuc.productservice.model.Product;
import cl.duocuc.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Controller", description = "API para gestión de productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public CollectionModel<EntityModel<Product>> getProducts() {
        List<Product> products = productService.findAll();

        List<EntityModel<Product>> productModels = products.stream()
                .map(product -> EntityModel.of(product,
                        linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(productModels,
                linkTo(methodOn(ProductController.class).getProducts()).withSelfRel()
        );
    }

    @Operation(summary = "Listar producto por identificador")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> getProductById(@PathVariable String id) {
        Product product = productService.findById(id);
        if (product != null) {
            EntityModel<Product> productResource = EntityModel.of(product);
            productResource.add(linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel());
            productResource.add(linkTo(methodOn(ProductController.class).getProducts()).withRel("all-products"));
            return ResponseEntity.ok(productResource);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear productos")
    @PostMapping
    public ResponseEntity<MessageResponse> createProduct(@RequestBody Product request) {
        boolean added = productService.addProduct(request);
        if (!added) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Producto ya existe"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Producto creado"));
    }

    @Operation(summary = "Eliminar productos por identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable String id) {
        boolean removed = productService.removeProduct(id);
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Producto eliminado"));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar productos por identificador")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> replaceProduct(@PathVariable String id, @RequestBody Product request) {
        boolean updated = productService.updateProduct(id, request);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Producto actualizado"));
        }
        return ResponseEntity.notFound().build();
    }
}

