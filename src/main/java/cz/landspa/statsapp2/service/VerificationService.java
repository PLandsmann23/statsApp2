package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.model.entity.user.VerificationToken;
import org.springframework.stereotype.Service;

@Service
public interface VerificationService {
    VerificationToken getTokenByUser(User user);

}
