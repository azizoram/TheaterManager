package userservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany
    @JoinTable(
    private List<User> users = new ArrayList<>();
}
