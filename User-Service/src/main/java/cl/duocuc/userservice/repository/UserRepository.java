package cl.duocuc.userservice.repository;

import cl.duocuc.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
