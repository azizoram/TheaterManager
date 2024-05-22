package userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userservice.model.*;
import userservice.repository.RoleRepository;
import userservice.utils.Utility;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserService userService;
    @Autowired
    public RoleService(RoleRepository roleRepository,@Lazy UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    private UserRoleEnum stringToEnum(String userRole) {
        return UserRoleEnum.fromString(userRole);
    }

    public void prefillRoles() {
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (roleRepository.findByName(role) == null) {
                roleRepository.save(UserRole.builder().name(role).build());
            }
        }
    }

    public void addRolesToUser(User user, List<String> userRoles) {
        List<UserRole> roles = userRoles.stream().map(this::stringToEnum).map(roleRepository::findByName).filter(Objects::nonNull).toList();
        if (roles.size() != userRoles.size()) {
            log.error("Some roles were not found");
            //TODO figure out handling
            return;
        }
        user.getUserRoles().addAll(roles);
        userService.saveOrUpdate(user);
    }

    public void addRole(Long id, String role) {
        User user = Utility.tryFindUser(id, userService);
        UserRole userRole = roleRepository.findByName(UserRoleEnum.fromString(role));
        if (userRole == null) {
            log.error("Role not found");
            return;
        }
        user.getUserRoles().add(userRole);
        userService.saveOrUpdate(user);
    }

    public void removeRole(Long id, String role) {
        User user = Utility.tryFindUser(id, userService);
        UserRole userRole = roleRepository.findByName(UserRoleEnum.fromString(role));
        if (userRole == null) {
            log.error("Role not found");
            return;
        }
        user.getUserRoles().remove(userRole);
        userService.saveOrUpdate(user);
    }

    public void removeRolesFromUser(User user, List<String> roles) {
        List<UserRole> userRoles = roles.stream().map(this::stringToEnum).map(roleRepository::findByName).filter(Objects::nonNull).toList();
        if (userRoles.size() != roles.size()) {
            log.error("Some roles were not found");
            //TODO figure out handling
            return;
        }
        user.getUserRoles().removeAll(userRoles);
        userService.saveOrUpdate(user);
    }
}
