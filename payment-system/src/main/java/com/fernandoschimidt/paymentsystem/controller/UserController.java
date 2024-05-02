package com.fernandoschimidt.paymentsystem.controller;

import com.fernandoschimidt.paymentsystem.dto.AuthenticationRequest;
import com.fernandoschimidt.paymentsystem.dto.AuthenticationResponse;
import com.fernandoschimidt.paymentsystem.dto.UserRequest;
import com.fernandoschimidt.paymentsystem.dto.UserResponse;
import com.fernandoschimidt.paymentsystem.entity.User;
import com.fernandoschimidt.paymentsystem.service.UserService;
import com.fernandoschimidt.paymentsystem.service.auth.TokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) throws MessagingException {
        User user = userRequest.toModel();
        UserResponse userSaved = userService.registerUser(user);
        return ResponseEntity.ok().body(userSaved);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequest req) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/teste")
    public  String teste(){
        return "TESTE OK";
    }
}
