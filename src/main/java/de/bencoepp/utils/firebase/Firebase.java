package de.bencoepp.utils.firebase;

import de.bencoepp.utils.firebase.entity.User;
import de.bencoepp.utils.firebase.service.UserService;

import java.io.IOException;

public class Firebase {

    private User user;

    private String apiKey;
    private UserService userService;

    public Firebase(String apiKey) {
        this.apiKey = apiKey;
        this.userService = new UserService(apiKey);
    }

    public void init() throws IOException {
        this.user = loginUser("test@test.de","elfenlied6");
    }

    public User signUpUser(String email, String password) throws IOException {
        return userService.signUpUser(email,password);
    }

    private User loginUser(String email, String password) throws IOException {
        return userService.loginUser(email,password);
    }
}
