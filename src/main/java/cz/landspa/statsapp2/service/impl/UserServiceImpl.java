package cz.landspa.statsapp2.service.impl;

import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.model.entity.user.UserSetting;
import cz.landspa.statsapp2.model.entity.user.VerificationToken;
import cz.landspa.statsapp2.repository.UserRepository;
import cz.landspa.statsapp2.repository.VerificationTokenRepository;
import cz.landspa.statsapp2.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String username) {
        return userRepository.findByUsernameOrEmail(username);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Uživatel nebyl nalezen"));

        if(user.getUsername() != null) existingUser.setUsername(user.getUsername());
        if(user.getEmail() != null) existingUser.setEmail(user.getEmail());
        if(user.getPassword() != null) existingUser.setPassword(user.getPassword());

        return userRepository.save(existingUser);
    }

    @Override
    public void activateUser(String token) {

            VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(()-> new IllegalArgumentException("Token nebyl nalezen"));
            verificationTokenRepository.delete(verificationToken);
            User user = verificationToken.getUser();
            if (user.isEnabled()) {
                throw new IllegalStateException("Uživatel je již aktivován.");
            }

            user.setEnabled(true);
            userRepository.save(user);

    }

    @Override
    public UserSetting updateUserSetting(Long userId, UserSetting newSettings) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Uživatel nebyl nalezen"));

        UserSetting settings = user.getUserSetting();

        if (settings == null) {
            settings = new UserSetting();
            settings.setUser(user);
        }

        if (newSettings.getDefaultPeriods() != null && newSettings.getDefaultPeriods() < 1) {
            throw new IllegalArgumentException("Počet period musí být alespoň 1");
        }
        if (newSettings.getDefaultPeriodLength() != null && newSettings.getDefaultPeriodLength() < 1) {
            throw new IllegalArgumentException("Délka periody musí být alespoň 1 minuta");
        }

        if (newSettings.getDefaultPeriods() != null) {
            settings.setDefaultPeriods(newSettings.getDefaultPeriods());
        }

        if (newSettings.getDefaultPeriodLength() != null) {
            settings.setDefaultPeriodLength(newSettings.getDefaultPeriodLength());
        }

        user.setUserSetting(settings);

        userRepository.save(user);

        return settings;
    }

}
