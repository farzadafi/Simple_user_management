package ir.farzadafi.repository;

import ir.farzadafi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :id")
    void updatePassword(@Param("newPassword") String newPassword, @Param("id") int id);

    @Modifying
    @Query("UPDATE User u SET u.enabled = true WHERE u.id = :id")
    void enable(@Param("id") int id);

    Optional<User> findByEmail(String email);
}
