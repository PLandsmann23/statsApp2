package cz.landspa.statsapp2.repository;

import cz.landspa.statsapp2.model.entity.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {


    Optional<VerificationToken> findByToken(String token);
}
