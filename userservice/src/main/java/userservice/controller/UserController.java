package userservice.controller;


import lombok.extern.slf4j.XSlf4j;
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
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user")
public class UserController {
      private final RoleService roleService;
    private final PermissionService permissionService;
    private final ExternalService externalService;
    private final Logger log = Logger.getLogger(UserController.class.getName());
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
        log.info("Getting all users");
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

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllEmployeeFromShift(@RequestBody List<Long> employyIds) {
        return userService.getAllUserWithsIds(employyIds);
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


}
