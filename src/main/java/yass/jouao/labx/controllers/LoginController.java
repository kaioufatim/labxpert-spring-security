package yass.jouao.labx.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @GetMapping("/user")
    public String getUser() {
        return "Welcome, User";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Welcome, Admin";
    }
    //@PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/dataTest")
    public Map<String, Object> dataTest(Authentication authentication){
        return Map.of(
                "message", "Data test",
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );

    }
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("/saveData")
    public Map<String, Object> saveData(String data){
        return Map.of(
                "data saved", data);


    }

}