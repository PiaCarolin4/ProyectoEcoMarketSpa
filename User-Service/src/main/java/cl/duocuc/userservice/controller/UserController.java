package cl.duocuc.userservice.controller;

import cl.duocuc.userservice.controller.response.MessageResponse;
import cl.duocuc.userservice.model.User;
import cl.duocuc.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "API para gestión de usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public CollectionModel<EntityModel<User>> getUsers() {
        List<User> users = userService.findAll();

        List<EntityModel<User>> userModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(userModels,
                linkTo(methodOn(UserController.class).getUsers()).withSelfRel()
        );
    }

    @Operation(summary = "Listar usuario por identificador")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable String id) {
        User user = userService.findById(id);
        if (user != null) {
            EntityModel<User> userResource = EntityModel.of(user);
            userResource.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
            userResource.add(linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"));
            return ResponseEntity.ok(userResource);
        }
        return ResponseEntity.notFound().build();
    }
    @Operation(summary = "Crear usuario")
    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@RequestBody User request) {
        boolean created = userService.addUser(request);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Usuario creado: " + "id = " + request.getId() +  " email = " + request.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Error: usuario ya existe: " + "id = " + request.getId() +  " email = " + request.getEmail()));
    }

    @Operation(summary = "Eliminar usuario por identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String id) {
        boolean deleted = userService.removeUser(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Usuario eliminado"));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar usuario por identificador")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> replaceUser(@PathVariable String id, @RequestBody User request) {
        boolean updated = userService.updateUser(id, request);
        if (updated) {
            return ResponseEntity.ok(new MessageResponse("Usuario actualizado"));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Activar usuario por identificador")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<MessageResponse> activateUser(@PathVariable String id) {
        boolean activated = userService.activateUser(id);
        if (activated) {
            return ResponseEntity.ok(new MessageResponse("Usuario activado"));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Desactivar usuario por identificador")
    @PatchMapping("/{id}/desactivate")
    public ResponseEntity<MessageResponse> desactivateUser(@PathVariable String id) {
        boolean deactivated = userService.desactivateUser(id);
        if (deactivated) {
            return ResponseEntity.ok(new MessageResponse("Usuario desactivado"));
        }
        return ResponseEntity.notFound().build();
    }
}