package cl.duocuc.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String rut;
    private String email;
    private String password;
    private boolean active;
    private String rol;
}
