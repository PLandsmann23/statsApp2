package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :username")
    Optional<User> findByUsernameOrEmail(String username);

}
