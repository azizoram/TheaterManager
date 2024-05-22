package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import userservice.model.*;

public interface PermissionRepository  extends JpaRepository<UserPermission, Long>{
//    UserRole findByRole(UserRoleEnum role);

    UserPermission findByName(UserPermissionEnum permission);
}
