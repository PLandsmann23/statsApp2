package cz.landspa.statsapp2.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest req, Exception ex, Model model) {
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("path", req.getRequestURI());

        return "error/error";
    }


    @RequestMapping("/error")
    public String handleDefaultError(HttpServletRequest req, Model model) {
        int status = 500;
        Object statusCode = req.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode != null) {
            try {
                status = Integer.parseInt(statusCode.toString());
            } catch (NumberFormatException ignored) {}
        }

        String view = switch (status) {
            case 404 -> "error/404";
            case 403 -> "error/403";
            default -> "error/error";
        };

        String errorMsg = String.valueOf(req.getAttribute("jakarta.servlet.error.message"));
        String path = String.valueOf(req.getAttribute("jakarta.servlet.error.request_uri"));

        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("status", status);
        model.addAttribute("error", HttpStatus.valueOf(status).getReasonPhrase());
        model.addAttribute("message", errorMsg);
        model.addAttribute("path", path);

        return view;
    }

}
