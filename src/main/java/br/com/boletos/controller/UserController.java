package br.com.boletos.controller;

import br.com.boletos.application.Config;
import br.com.boletos.application.Messages;
import br.com.boletos.application.ResponseObject;
import br.com.boletos.model.User;
import br.com.boletos.service.AuthService;
import br.com.boletos.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

@RestController
public class UserController {

    /**
     * Injeção do serviço de usuário.
     */
    @Autowired
    private UserService userService;

    /**
     * Método que busca todos os usuarios registrados
     *
     * @return
     */
    @GetMapping("api/users")
    public ResponseObject getAllUsers(HttpServletRequest request) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, userService.getAll());
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que salva um novo usuario
     *
     * @param user
     * @return
     */
    @PostMapping("api/users")
    public ResponseObject saveUser(@RequestBody User user) {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, userService.save(user));
        } catch (ValidationException e) {
            return new ResponseObject(406, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método responsável por realizar o login do usuário.
     *
     * @param userData O objeto userData será serializado, deverá conter ao menos seu username e password
     * @return ResponseObject contendo um token para acesso do usuário
     */
    @PostMapping("api/users/login")
    public ResponseObject login(@RequestBody User userData) {
        User user = userService.getSingleByUsernameAndPassword(userData.getUsername(), AuthService.generateHash(userData.getPassword()));
        if (user == null)
            return new ResponseObject(406, "Usuário e/ou senha inválido(s)!", null);
        String jwt = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(SignatureAlgorithm.HS256, Config.KEY_AUTHENTICATION)
                .compact();
        return new ResponseObject(200, Messages.MESSAGE_SUCCESS, jwt);
    }

    /**
     * Método que retorna todas as situações dos usuarios
     *
     * @return
     */
    @GetMapping("api/users/situations")
    public ResponseObject getSituations() {
        try {
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, userService.getAllSituations());
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }

    /**
     * Método que verifica se o usuario está locado
     *
     * @param request
     * @return
     */
    @GetMapping("api/users/auth")
    public ResponseObject getAutentication(HttpServletRequest request) {
        try {
            if (!AuthService.isLoggedIn(request))
                return AuthService.responseNotAuthorized();
            return new ResponseObject(200, Messages.MESSAGE_SUCCESS, null);
        } catch (Exception e) {
            return new ResponseObject(500, Messages.MESSAGE_FAIL, null);
        }
    }
}