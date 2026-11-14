package pe.edu.cibertec.demopruebasmockito.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Test
    @DisplayName("Debe lanzar Excepcion si el correo existe")
    void testDebeLanzarExcepcionSiElCorreoExiste() {
        String correo = "luis@cibertec.edu.pe";
        UserRegistrationRequest request = new UserRegistrationRequest(
                correo, "pass123");

        when(userRepository.existsByEmail(correo)).thenReturn(true);

        assertThatThrownBy(() -> userRegistrationService
                .registerUser(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("correo electrónico " +
                        "ya existe");
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }


    @Test
    @DisplayName("Debe registrar al usuario con el password encriptado")
    void testDebeRegistrarAlUsuarioConPasswordEncriptado() {
        String correo = "luis@cibertec.edu.pe";
        String password = "pass123";
        String passwordEncriptado = "pass123_encriptado";

        UserRegistrationRequest request = new UserRegistrationRequest(
                correo, password);

        when(userRepository.existsByEmail(correo)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(passwordEncriptado);

        User usuarioGuardado = new User(correo,
                passwordEncriptado);
        when(userRepository.save(any(User.class)))
                .thenReturn(usuarioGuardado);

        User result =  userRegistrationService.registerUser(request);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(correo);
        assertThat(result.password()).isEqualTo(passwordEncriptado);

        verify(userRepository, times(1))
                .save(any(User.class));
    }

}
