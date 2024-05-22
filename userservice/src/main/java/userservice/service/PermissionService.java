package userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import userservice.model.User;
import userservice.model.UserPermission;
import userservice.model.UserPermissionEnum;
import userservice.repository.PermissionRepository;
import userservice.utils.Utility;

import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserService userService;
    @Autowired
    public PermissionService(PermissionRepository permissionRepository, @Lazy UserService userService) {
        this.permissionRepository = permissionRepository;
        this.userService = userService;
    }

    public void prefillPermissions() {
        for (UserPermissionEnum permission : UserPermissionEnum.values()) {
            if (permissionRepository.findByName(permission) == null) {
                permissionRepository.save(UserPermission.builder().name(permission).build());
            }
        }
    }

    private UserPermissionEnum stringToEnum(String userPermission) {
        return UserPermissionEnum.fromString(userPermission);
    }

    public void addPermissionsToUser(User user, List<String> userPermission) {

        List<UserPermission> permissions = userPermission.stream().map(this::stringToEnum).map(permissionRepository::findByName).filter(Objects::nonNull).toList();
        if (permissions.size() != userPermission.size()) {
            log.error("Some permissions were not found");
            //TODO figure out handling
            return;
        }
        user.getUserPermissions().addAll(permissions);
        userService.saveOrUpdate(user);
    }

    public void addPermissionToUser(Long id, String permission) {
        User user = Utility.tryFindUser(id, userService);
        UserPermission userPermission = permissionRepository.findByName(UserPermissionEnum.valueOf(permission));
        if (userPermission == null) {
            log.error("Permission not found");
            return;
        }
        user.getUserPermissions().add(userPermission);
        userService.saveOrUpdate(user);
    }

    public void removePermissionFromUser(Long id, String permission) {
        User user = Utility.tryFindUser(id, userService);
        UserPermission userPermission = permissionRepository.findByName(UserPermissionEnum.valueOf(permission));
        if (userPermission == null) {
            log.error("Permission not found");
            return;
        }
        user.getUserPermissions().remove(userPermission);
        userService.saveOrUpdate(user);
    }

    public void removePermissionsFromUser(User user, List<String> permissions) {
        List<UserPermission> userPermissions = permissions.stream().map(this::stringToEnum).map(permissionRepository::findByName).filter(Objects::nonNull).toList();
        if (userPermissions.size() != permissions.size()) {
            log.error("Some permissions were not found");
            //TODO figure out handling
            return;
        }
        userPermissions.forEach(user.getUserPermissions()::remove);
        userService.saveOrUpdate(user);
    }
}
