package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import userservice.model.UserRole;
import userservice.model.UserRoleEnum;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(UserRoleEnum userRoleEnum);
}
