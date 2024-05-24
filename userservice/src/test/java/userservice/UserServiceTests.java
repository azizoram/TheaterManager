package userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import userservice.dto.UserDTO;
import userservice.model.User;
import userservice.repository.UserRepository;
import userservice.service.PermissionService;
import userservice.service.RoleService;
import userservice.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class UserServiceTests {

    @Mock
    private PermissionService permissionService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // create user test
    @Test
    void createUser_ShouldSaveUserWithPermissionsAndRoles() {
        UserDTO userDTO = UserDTO.builder()
                .name("John")
                .surname("Doe")
                .login("johndoe")
                .email("john.doe@example.com")
                .password("password")
                .phone("1234567890")
                .userPermissions(Arrays.asList("READ", "WRITE"))
                .userRoles(Arrays.asList("ADMIN"))
                .build();

        User user = User.builder()
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phone(userDTO.getPhone())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(userDTO);

        verify(permissionService, times(1)).addPermissionsToUser(any(User.class), eq(userDTO.getUserPermissions()));
        verify(roleService, times(1)).addRolesToUser(any(User.class), eq(userDTO.getUserRoles()));
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void updateUserInfo_ShouldUpdateAndSaveUser() {
        User existingUser = User.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .login("johndoe")
                .email("john.doe@example.com")
                .password("password")
                .phone("1234567890")
                .build();

        UserDTO updateUserDTO = UserDTO.builder()
                .name("Jane")
                .surname("Doe")
                .login("janedoe")
                .email("jane.doe@example.com")
                .password("newpassword")
                .phone("0987654321")
                .userPermissions(Arrays.asList("READ", "WRITE"))
                .userRoles(Arrays.asList("USER"))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.updateUserInfo(1L, updateUserDTO);

        assertEquals("Jane", existingUser.getName());
        assertEquals("Doe", existingUser.getSurname());
        assertEquals("janedoe", existingUser.getLogin());
        assertEquals("jane.doe@example.com", existingUser.getEmail());
        assertEquals("newpassword", existingUser.getPassword());
        assertEquals("0987654321", existingUser.getPhone());
        verify(permissionService, times(1)).addPermissionsToUser(existingUser, updateUserDTO.getUserPermissions());
        verify(roleService, times(1)).addRolesToUser(existingUser, updateUserDTO.getUserRoles());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
    @Test
    void findById_ShouldReturnUser() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .login("johndoe")
                .email("john.doe@example.com")
                .password("password")
                .phone("1234567890")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnNullIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.findById(1L);

        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
    }
}
