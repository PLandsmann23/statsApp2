package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.user.ChangePassword;
import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.model.entity.user.UserSetting;
import cz.landspa.statsapp2.model.entity.user.VerificationToken;
import cz.landspa.statsapp2.service.UserService;
import cz.landspa.statsapp2.service.VerificationService;
import cz.landspa.statsapp2.util.SecurityUtil;
import cz.landspa.statsapp2.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final VerificationService verificationService;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, VerificationService verificationService, SecurityUtil securityUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.verificationService = verificationService;
        this.securityUtil = securityUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user, HttpServletRequest request) {
        User newUser = userService.saveUser(user);
        VerificationToken token = verificationService.getTokenByUser(newUser);
        String url = Util.getUrl(request) + "/activate?token=" + token.getToken();
      //  emailService.sendVerificationEmail(newUser.getEmail(), url);

        return ResponseEntity.created(URI.create("/api/users/" + newUser.getId())).body(newUser);
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword) {
        User user = securityUtil.getCurrentUser();

        if (!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Staré heslo není správné");
        }

        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userService.updateUser(user.getId(), user);

        return ResponseEntity.ok(Map.of("message", "Heslo bylo úspěšně změněno"));
    }

    @PostMapping("/settings")
    public ResponseEntity<?> changeSettings(@Valid @RequestBody UserSetting userSetting) {
        User user = securityUtil.getCurrentUser();
        userSetting.setUser(user);

        UserSetting updatedSettings = userService.updateUserSetting(user.getId(), userSetting);

        return ResponseEntity.ok(updatedSettings);
    }

}
