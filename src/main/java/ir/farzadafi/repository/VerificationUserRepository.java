package ir.farzadafi.repository;

import ir.farzadafi.model.VerificationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationUserRepository extends JpaRepository<VerificationUser, Integer> {
    Optional<VerificationUser> findByCode(int code);
}
