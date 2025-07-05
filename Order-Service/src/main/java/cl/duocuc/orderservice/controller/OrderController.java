package cl.duocuc.orderservice.controller;

import cl.duocuc.orderservice.controller.response.MessageResponse;
import cl.duocuc.orderservice.model.Order;
import cl.duocuc.orderservice.service.OrderService;
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
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "API para gestión de órdenes")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Listar todas las órdenes")
    @GetMapping
    public CollectionModel<EntityModel<Order>> getOrders() {
        List<Order> orders = orderService.findAll();

        List<EntityModel<Order>> orderModels = orders.stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(orderModels,
                linkTo(methodOn(OrderController.class).getOrders()).withSelfRel()
        );
    }

    @Operation(summary = "Listar orden por identificador")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Order>> getOrderById(@PathVariable String id) {
        Order order = orderService.findById(id);
        if (order != null) {
            EntityModel<Order> orderResource = EntityModel.of(order);
            orderResource.add(linkTo(methodOn(OrderController.class).getOrderById(id)).withSelfRel());
            orderResource.add(linkTo(methodOn(OrderController.class).getOrders()).withRel("all-orders"));
            return ResponseEntity.ok(orderResource);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear ordenes")
    @PostMapping
    public ResponseEntity<MessageResponse> createOrder(@RequestBody Order request) {
        boolean added = orderService.addOrder(request);
        if (!added) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Orden ya existe"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Orden creada"));
    }

    @Operation(summary = "Eliminar orden por identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteOrder(@PathVariable String id) {
        boolean removed = orderService.removeOrder(id);
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Orden eliminada"));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar orden por identificador")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateOrder(@PathVariable String id, @RequestBody Order request) {
        boolean updated = orderService.updateOrder(id, request);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Orden actualizada"));
        }
        return ResponseEntity.notFound().build();
    }
}