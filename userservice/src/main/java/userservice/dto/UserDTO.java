package userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import userservice.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserDTO implements Serializable {
        private Long id;
        private String name;
        private String surname;
        private String login;
        private String email;
        private String password;
        private String phone;
        private List<String> userPermissions = new ArrayList<>();
        private List<String> userRoles = new ArrayList<>();
        public UserDTO(User user){
            this.id = user.getId();
            this.name = user.getName();
            this.surname = user.getSurname();
            this.login = user.getLogin();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.phone = user.getPhone();
            this.userPermissions = user.getUserPermissions().stream().map(userPermission -> userPermission.getName().toString()).toList();
            this.userRoles = user.getUserRoles().stream().map(userRole -> userRole.getName().toString()).toList();
        }
    }
