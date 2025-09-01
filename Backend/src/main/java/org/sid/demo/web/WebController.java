package org.sid.demo.web;

import org.sid.demo.DTO.UserDto;
import org.sid.demo.Exception.UserNotAuthorizedException;
import org.sid.demo.entities.User;
import org.sid.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin( origins = "http://localhost:8080",
              allowCredentials = "true")

public class WebController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String HelloWorld(){
        return "Hello World" ;
    }


    @RequestMapping("/user")
    public Principal user(Principal user){

        return user;
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserInfo(@AuthenticationPrincipal OAuth2User principal) throws UserNotAuthorizedException {

        String login =principal.getAttribute("login");             // → Amine-serhriri
        String name = principal.getAttribute("name");               // → Mohemed Amine Serhiri
        String email = principal.getAttribute("email");             // → amineyassir2001@gmail.com
        String avatar_url = principal.getAttribute("avatar_url");   // → image GitHub
        String htmlUrl = principal.getAttribute("html_url");       // → lien GitHub

        ResponseEntity<String> validation = userService.validateUserAccess(email);
        if (!validation.getStatusCode().is2xxSuccessful()) {
            // renvoie l'erreur directement (403 par ex.)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UserDto user = new UserDto(login,name,email,avatar_url,htmlUrl);

       return ResponseEntity.ok(user) ;



    }

    }




