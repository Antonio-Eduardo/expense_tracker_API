package com.eduardo.expense_tracker.unit.service;

import com.eduardo.expense_tracker.dtos.request.RegisterDTO;
import com.eduardo.expense_tracker.dtos.request.UserDTO;
import com.eduardo.expense_tracker.entities.user.User;
import com.eduardo.expense_tracker.entities.user.UserRole;
import com.eduardo.expense_tracker.repositories.UserRepository;
import com.eduardo.expense_tracker.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void deveriaAtualizarUmUsuario(){
        User user = new User();
        user.setId(1L);
        user.setName("antigo");

        UserDTO userDTO = new UserDTO();
        userDTO.setName("novo");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(user.getId(), userDTO);

        assertNotNull(result);
        assertEquals("novo", result.getName());

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(any(User.class));
    }
    @Test
    public void deveriaDeletarUmUsuario() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
    @Test
    public void deveriaCriarUmUsuarioComEmailEsenha() {
        RegisterDTO registerDTO = new RegisterDTO("eduardo@gmail.com", "password", UserRole.USER);
        User user = new User();
        user.setEmail(registerDTO.email());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(registerDTO);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        assertEquals(registerDTO.email(), result.getEmail());
    }

    @Test
    public void deveriaEncontrarUmUsuarioPorEmail(){
        User user = new User();
        user.setEmail("eduardo@gmail.com");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        User result = userService.findByEmail(user.getEmail());

        verify(userRepository).findByEmail(any(String.class));
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }
    @Test
    public void deveriaRetornarTodosOsUsuarios(){
        List<User> users = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.userFindAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
}
