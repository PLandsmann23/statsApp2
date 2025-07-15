package cz.landspa.statsapp2.controller.api;


import cz.landspa.statsapp2.model.DTO.email.SupportEmail;
import cz.landspa.statsapp2.model.entity.user.User;
import cz.landspa.statsapp2.service.EmailService;
import cz.landspa.statsapp2.service.UserService;
import cz.landspa.statsapp2.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/support")
public class SupportController {

    private final UserService userService;

    private final SecurityUtil securityUtil;
    private final EmailService emailService;

    public SupportController(UserService userService, SecurityUtil securityUtil, EmailService emailService) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendSupportEmail(@RequestBody SupportEmail supportEmail){
            User user = securityUtil.getCurrentUser();
            supportEmail.setEmail(user.getEmail());

            emailService.sendEmail(supportEmail.getEmail(), "support@patriklandsmann.cz", supportEmail.getSubject(), supportEmail.getMessage());

            return new ResponseEntity<>(HttpStatus.OK);

    }
}
