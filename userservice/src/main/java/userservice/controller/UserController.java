package userservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import userservice.dto.PerformanceDTO;
import userservice.dto.WorkshiftDTO;
import userservice.model.User;
import userservice.service.ExternalService;
import userservice.service.PermissionService;
import userservice.service.RoleService;
import userservice.service.UserService;
import userservice.dto.UserDTO;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
      private final RoleService roleService;
    private final PermissionService permissionService;
    private final ExternalService externalService;
    @Lazy
    private final UserService userService;

    @Autowired
    public UserController(RoleService roleService, PermissionService permissionService, ExternalService externalService, UserService userService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.externalService = externalService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserDTO userRequest) {
        userService.createUser(userRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserWithId(id);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    @PostMapping("update/{id}")
    public void updateUserInfo(@PathVariable Long id, @RequestBody UserDTO userRequest) {
        userService.updateUserInfo(id, userRequest);
    }

    // ROLES
    @PostMapping("/{id}/addRole")
    @ResponseStatus(HttpStatus.OK)
    public void addRoleToUSer(@PathVariable Long id, @RequestBody HashMap<String, String> role) {
        roleService.addRole(id, role.get("role"));
    }

    @PostMapping("/populateRoles")
    @ResponseStatus(HttpStatus.OK)
    public void populateRoles() {
        roleService.prefillRoles();
    }


    @DeleteMapping("/{id}/removeRole")
    @ResponseStatus(HttpStatus.OK)
    public void removeRoleFromWorkShift(@PathVariable Long id, @RequestBody String role) {
        roleService.removeRole(id, role);
    }

    @PostMapping("/{id}/roles")
    @ResponseStatus(HttpStatus.OK)
    public void addRolesToUser(@PathVariable Long id, @RequestBody List<String> roles) {
        User user = userService.findById(id);
        roleService.addRolesToUser(user, roles);
    }

    @DeleteMapping("/{id}/roles")
    @ResponseStatus(HttpStatus.OK)
    public void removeRolesFromUser(@PathVariable Long id, @RequestBody List<String> roles) {
        User user = userService.findById(id);
        roleService.removeRolesFromUser(user, roles);
    }

    @GetMapping("/{id}/roles")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getRoles(@PathVariable Long id) {
        User user = userService.findById(id);
        return user.getUserRoles().stream().map(role -> role.getName().toString()).toList();
    }
    // PERMISSIONS
    @PostMapping("/{id}/addPermission")
    @ResponseStatus(HttpStatus.OK)
    public void addPermissionToUser(@PathVariable Long id, @RequestBody String permission) {
        permissionService.addPermissionToUser(id, permission);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void removePermissionFromUser(@PathVariable Long id, @RequestBody String permission) {
        permissionService.removePermissionFromUser(id, permission);
    }

    @PostMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    public void addPermissionsToUser(@PathVariable Long id, @RequestBody List<String> permissions) {
        User user = userService.findById(id);
        permissionService.addPermissionsToUser(user, permissions);
    }

    @DeleteMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    public void removePermissionsFromUser(@PathVariable Long id, @RequestBody List<String> permissions) {
        User user = userService.findById(id);
        permissionService.removePermissionsFromUser(user, permissions);
    }

    @GetMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getPermissions(@PathVariable Long id) {
        User user = userService.findById(id);
        return user.getUserPermissions().stream().map(permission -> permission.getName().toString()).toList();
    }

    @PostMapping("addWorkShift/{id}/workShift/{workShiftId}")
    @ResponseStatus(HttpStatus.OK)
    public void addWorkShiftToUser(@PathVariable Long id, @PathVariable Long workShiftId) {
        userService.addWorkShiftToUser(id, workShiftId);
    }

    @DeleteMapping("removeWorkShift/{id}/workShift/{workShiftId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeWorkShiftFromUser(@PathVariable Long id, @PathVariable Long workShiftId) {
        userService.removeWorkShiftFromUser(id, workShiftId);
    }

    @GetMapping("/{id}/workShiftIDs")
    @ResponseStatus(HttpStatus.OK)
    public List<Long> getWorkShifts(@PathVariable Long id) {
        User user = userService.findById(id);
        return user.getWorkShifts().stream().toList();
    }

    @GetMapping("find/{id}/workShifts")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkshiftDTO> getWorkShiftList(@PathVariable Long id) {
        User user = userService.findById(id);
        return externalService.getWorkShifts(user.getWorkShifts());
    }

    @GetMapping("find/{id}/performances")
    @ResponseStatus(HttpStatus.OK)
    public List<PerformanceDTO> getPerformanceList(@PathVariable Long id) {
        User user = userService.findById(id);
        return externalService.getPerformances(user.getWorkShifts());
    }
}
