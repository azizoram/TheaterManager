package userservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_permission")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private UserPermissionEnum name;

    @ManyToMany(mappedBy = "userPermissions")
    private Set<User> users = new HashSet<>();
}
