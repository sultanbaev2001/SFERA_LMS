package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
