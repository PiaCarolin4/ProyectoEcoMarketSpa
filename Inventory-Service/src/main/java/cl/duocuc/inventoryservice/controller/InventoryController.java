package cl.duocuc.inventoryservice.controller;

import cl.duocuc.inventoryservice.controller.response.MessageResponse;
import cl.duocuc.inventoryservice.model.Inventory;
import cl.duocuc.inventoryservice.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Controller", description = "API para gestión de inventario")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Listar todo el inventario")
    @GetMapping
    public CollectionModel<EntityModel<Inventory>> getInventory() {
        List<Inventory> inventory = inventoryService.findAll();

        List<EntityModel<Inventory>> inventoryModels = inventory.stream()
                .map(product -> EntityModel.of(product,
                        linkTo(methodOn(InventoryController.class).getInventoryById(product.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(inventoryModels,
                linkTo(methodOn(InventoryController.class).getInventory()).withSelfRel()
        );
    }

    @Operation(summary = "Listar inventario de cierto producto")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventory>> getInventoryById(@PathVariable String id) {
        Inventory inventory = inventoryService.findById(id);
        if (inventory != null) {
            EntityModel<Inventory> inventoryResource = EntityModel.of(inventory);
            inventoryResource.add(linkTo(methodOn(InventoryController.class).getInventoryById(id)).withSelfRel());
            inventoryResource.add(linkTo(methodOn(InventoryController.class).getInventory()).withRel("all-inventorys"));
            return ResponseEntity.ok(inventoryResource);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear inventario de cierto producto")
    @PostMapping
    public ResponseEntity<MessageResponse> createInventory(@RequestBody Inventory request) {
        boolean added = inventoryService.addInventory(request);
        if (!added) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Producto ya existe en el inventario, por favor modifique"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Producto añadido al inventario"));
    }

    @Operation(summary = "Eliminar producto de inventario por su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteInventory(@PathVariable String id) {
        boolean removed = inventoryService.removeInventory(id);
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Producto eliminado del inventario"));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar inventario de producto por su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateInventory(@PathVariable String id, @RequestBody Inventory request) {
        boolean updated = inventoryService.updateInventory(id, request);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Inventario del producto actualizado"));
        }
        return ResponseEntity.notFound().build();
    }
}
