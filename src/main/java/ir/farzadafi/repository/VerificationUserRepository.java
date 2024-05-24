package ir.farzadafi.repository;

import ir.farzadafi.model.VerificationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificationUserRepository extends JpaRepository<VerificationUser, Integer> {
    Optional<VerificationUser> findByCode(int code);

    @Modifying
    @Query("DELETE FROM VerificationUser v WHERE v.id = :id")
    void customDelete(@Param("id") int id);
}
