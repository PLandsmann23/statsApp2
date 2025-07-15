package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.model.entity.user.VerificationToken;
import cz.landspa.statsapp2.service.VerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {
    @Override
    public VerificationToken getTokenByUser(User user) {
        return null;
    }
}
