package cz.landspa.statsapp2.service;

import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.model.entity.user.UserSetting;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User saveUser(User user);

    Optional<User> getUserByUsernameOrEmail(String username);

    void deleteUser(Long id);

    Optional<User> findById(Long id);

    User updateUser(Long id, User user);

    void activateUser(String token);

    UserSetting updateUserSetting(Long userId, UserSetting newSettings);

}
