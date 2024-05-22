package userservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private UserRoleEnum name;
    @ManyToMany(mappedBy = "userRoles")
    private Set<User> users = new HashSet<>();
}
