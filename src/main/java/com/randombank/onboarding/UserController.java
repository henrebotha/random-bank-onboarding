package com.randombank.onboarding;

import com.randombank.onboarding.exceptions.IncorrectPasswordException;
import com.randombank.onboarding.exceptions.NotAuthenticatedException;
import com.randombank.onboarding.exceptions.UserAddressInvalidException;
import com.randombank.onboarding.exceptions.UserAlreadyExistsException;
import com.randombank.onboarding.exceptions.UserCountryInvalidException;
import com.randombank.onboarding.exceptions.UserNotFoundException;
import com.randombank.onboarding.exceptions.UserTooYoungException;
import com.randombank.onboarding.services.LoginService;
import com.randombank.onboarding.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/register")
    public User register(
            @RequestBody CreateUserDTO user
    ) {
        // Validate:
        //   username must be unique
        //   country must be NL/BE
        //   age must be >= 18
        // Generate:
        //   IBAN per NL format
        //   default password

        logger.info("/register request: {}", user.toString());
        return userService.create(user);
    }

    @GetMapping("/overview")
    public User overview(HttpSession session) {
        logger.info("/overview request: {}", session);
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            throw new RuntimeException("Username is null");
        }
        return userService.findByUsername(username);
    }

    @GetMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        logger.info("/login request: {} / {}", username, password);
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                password
        ));

        User user;
        try {
            user = (User) auth.getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException("argh");
        }
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(auth);
        this.securityContextHolderStrategy.setContext(context);

        return ResponseEntity.ok(user);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(
            HttpStatus.CONFLICT
    )
    private Map<String, String> handleUserAlreadyExistsException(
            UserAlreadyExistsException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserCountryInvalidException.class)
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    private Map<String, String> handleUserCountryInvalidException(
            UserCountryInvalidException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserAddressInvalidException.class)
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    private Map<String, String> handleUserAddressInvalidException(
            UserAddressInvalidException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserTooYoungException.class)
    @ResponseStatus(
            HttpStatus.BAD_REQUEST
    )
    private Map<String, String> handleUserTooYoungException(
            UserTooYoungException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = IncorrectPasswordException.class)
    @ResponseStatus(
            HttpStatus.UNAUTHORIZED
    )
    private Map<String, String> handleIncorrectPasswordException(
            IncorrectPasswordException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = NotAuthenticatedException.class)
    @ResponseStatus(
            HttpStatus.UNAUTHORIZED
    )
    private Map<String, String> handleNotAuthenticatedException(
            NotAuthenticatedException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(
            HttpStatus.NOT_FOUND
    )
    private Map<String, String> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        Map<String, String> body = new HashMap<>();
        body.put("error", e.getMessage());
        return body;
    }
}
