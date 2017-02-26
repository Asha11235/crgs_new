package responses;

import java.util.ArrayList;
import java.util.List;

import models.User;


public class LoginResponse {
    public String role;

    public LoginResponse(User user) {
        this.role = user.role.getRoleName();
    }
}
