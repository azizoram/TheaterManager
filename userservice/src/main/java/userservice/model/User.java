package userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tm_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String name;

    private String surname;

    private String login;

    private String email;

    private String password;

    private String phone;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserPermission> userPermissions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserRole> userRoles = new ArrayList<>();

}
