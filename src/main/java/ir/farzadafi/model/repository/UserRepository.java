package ir.farzadafi.model.repository;

import ir.farzadafi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
