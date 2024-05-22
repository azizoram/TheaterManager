package userservice.utils;

import userservice.exception.NoSuchUserException;
import userservice.model.User;
import userservice.repository.UserRepository;
import userservice.service.UserService;

public class Utility {
    public static User tryFindUser (long id, UserService userService) {
        User user = userService.findById(id);
        if (user == null) {
            throw new NoSuchUserException("No such user with id: " + id + " found!");
        }
        return user;
    }
}
