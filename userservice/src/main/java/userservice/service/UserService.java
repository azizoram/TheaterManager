package userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import userservice.dto.UserDTO;
import userservice.model.User;
import userservice.repository.UserRepository;
import userservice.utils.Utility;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final WebClient.Builder webClient;

    public void saveOrUpdate(User user) {
        userRepository.save(user);
    }

    private User dtoToNewUser(UserDTO userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .login(userRequest.getLogin())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .phone(userRequest.getPhone())
                .build();
    }

    public void createUser(UserDTO userRequest) {
        User user = dtoToNewUser(userRequest);
        permissionService.addPermissionsToUser(user, userRequest.getUserPermissions());
        roleService.addRolesToUser(user, userRequest.getUserRoles());
        userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).toList();
    }

    public UserDTO getUserWithId(Long id) {
        return userRepository.findById(id).map(UserDTO::new).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserInfo(Long id, UserDTO userRequest) {
        User user = Utility.tryFindUser(id, this);
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setLogin(userRequest.getLogin());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPhone(userRequest.getPhone());
        permissionService.addPermissionsToUser(user, userRequest.getUserPermissions());
        roleService.addRolesToUser(user, userRequest.getUserRoles());
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserDTO> getAllUserWithsIds(List<Long> employyIds) {
        return employyIds.stream().map(this::getUserWithId).filter(Objects::nonNull).toList();
    }
}
